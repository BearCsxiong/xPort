package me.csxiong.camera.opengl;

import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 持有Egl环境的线程.
 */
public class EglThread extends Thread {
    public static final String TAG = "EglThread";
    /**
     * Egl环境创建者。
     */
    private EglProducer mEglProducer;
    /**
     * 实现业务的渲染对象。
     */
    private AbsEglRenderer mEglRenderer;
    /**
     * 共享EGL环境。
     */
    private EGLContext mShareContext;
    /**
     * {@link android.view.Surface} 或者是 {@link android.graphics.SurfaceTexture}.
     */
    private Object mSurface;
    /**
     * 渲染锁。
     */
    private final Object mRenderLock = new Object();
    /**
     * 是否被要求销毁GL。
     */
    private boolean isRequestDestroy;
    /**
     * Surface的尺寸是否有改变。
     */
    private volatile boolean isSurfaceSizeChange = true;
    /**
     * Surface是否有改变。
     */
    private volatile boolean isSurfaceChange = true;
    /**
     * GL任务。
     */
    private CopyOnWriteArrayList<Runnable> mQueueTask = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Runnable> mAfterRenderTask = new CopyOnWriteArrayList<>();
    /**
     * Surface的尺寸。
     */
    private int mSurfaceWidth, mSurfaceHeight;
    /**
     * 请求渲染标志位，作用主要是防止requestRender被漏掉。
     */
    private volatile boolean needRenderFlag;

    public EglThread() {
        this(EGL14.EGL_NO_CONTEXT);
    }

    /**
     * @param shareContext 共享Egl环境，可以跨线程访问GL资源 ，不需要时传EGL10.EGL_NO_CONTEXT。
     */
    public EglThread(@NonNull EGLContext shareContext) {
        mShareContext = shareContext;
    }

    public void setEglRenderer(AbsEglRenderer mEglRenderer) {
        this.mEglRenderer = mEglRenderer;
    }

    public void setSurface(Object surface, int width, int height) {
        if (mSurface != surface) {
            mSurface = surface;
            isSurfaceChange = true;
        }
        if (mSurfaceWidth != width || mSurfaceHeight != height) {
            mSurfaceWidth = width;
            mSurfaceHeight = height;
            isSurfaceSizeChange = true;
        }
        synchronized (mRenderLock) {
            mRenderLock.notifyAll();
        }
    }

    /**
     * 获取当前GL线程的共享环境。
     * @return
     */
    public EGLContext getShareContext() {
        if (mEglProducer != null) {
            return mEglProducer.getEglContext();
        }
        if (mShareContext != EGL14.EGL_NO_CONTEXT) {
            return mShareContext;
        }
        return null;
    }

    @Override
    public void run() {
        mEglProducer = new EglProducer();
        mEglProducer.createEGL(mShareContext);

        while (true) {

            // 执行销毁。
            if (isRequestDestroy) {
//                StudioLogUtils.v(TAG, "销毁EGL.");
                mEglProducer.destroyEGL();
                isRequestDestroy = false;
                return;
            }

            // Surface发生改变。
            if (isSurfaceChange) {
//                StudioLogUtils.v(TAG, "Surface改变.");
                mEglProducer.createSurface(mSurface);
                if (mEglRenderer != null) {
                    mEglRenderer.onSurfaceCreated();
                }
                isSurfaceChange = false;
            }

            // Surface的尺寸发生改变。
            if (isSurfaceSizeChange) {
                if (mEglRenderer != null) {
                    mEglRenderer.onSurfaceChanged(mSurfaceWidth, mSurfaceHeight);
                }
                isSurfaceSizeChange = false;
            }

            // 执行队列任务，这边的同步手段是保证生产者不阻塞。
            List<Runnable> tasks = new ArrayList<>(mQueueTask);
            for (Runnable task : tasks) {
                task.run();
            }
            mQueueTask.removeAll(tasks);

            // 渲染。
            needRenderFlag = false;
            List<Runnable> afterRenderTasks = new ArrayList<>(mAfterRenderTask);
            if (mEglRenderer != null) {
                mEglRenderer.onDrawFrame();
            }
            if (mSurface != null) {
                // 离屏渲染不要调用此函数，部分机型会导致ANR
                mEglProducer.refreshSurface();
            }
            // 执行渲染完成回调。
            for (Runnable afterRenderTask : afterRenderTasks) {
                afterRenderTask.run();
            }
            mAfterRenderTask.removeAll(afterRenderTasks);

            synchronized (mRenderLock) {
                if (!mQueueTask.isEmpty() || needRenderFlag || isRequestDestroy || isSurfaceChange
                    || isSurfaceSizeChange) {
                    continue;
                }
                // 等待下一次唤醒。
                try {
                    mRenderLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 销毁环境。
     */
    public void onDestroy() {
        isRequestDestroy = true;
        synchronized (mRenderLock) {
            mRenderLock.notifyAll();
        }
    }

    /**
     * 要求渲染一次。
     */
    public void requestRender() {
        synchronized (mRenderLock) {
            needRenderFlag = true;
            mRenderLock.notifyAll();
        }
    }

    /**
     * 要求渲染一次。
     */
    public void requestRender(Runnable runnable) {
        synchronized (mRenderLock) {
            needRenderFlag = true;
            if (runnable != null) {
                mAfterRenderTask.add(runnable);
            }
            mRenderLock.notifyAll();
        }
    }

    /**
     * 将一个任务排入GL线程去做，会触发渲染。
     * @param runnable
     */
    public void queueEvent(Runnable runnable) {
        synchronized (mRenderLock) {
            mQueueTask.add(runnable);
            mRenderLock.notifyAll();
        }
    }
}
