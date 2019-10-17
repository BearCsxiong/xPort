package me.csxiong.library.utils.opengl;

import android.support.annotation.NonNull;

/**
 * Gl渲染环境提供者。
 * 可以是{@link android.opengl.GLSurfaceView},也可是客户段自己实现的离屏GL环境。
 */
public class TextureViewGLExecutor implements GLThreadExecutor {
    /**
     * GlSurfaceView，普通渲染线程。
     */
    private GLTextureView mTextureView;
    /**
     * 离屏线程，可用于环境加载。
     */
    private EglThread mShareEglThread;
    /**
     * 是否已经被销毁。
     */
    private boolean isDestroy;

    /**
     * 初始化GLContext。
     * @param surfaceView
     */
    public void initGlContext(@NonNull GLTextureView surfaceView) {
        this.mTextureView = surfaceView;
        mShareEglThread = new EglThread();
        mShareEglThread.setEglRenderer(new AbsEglRenderer() {
            @Override
            public void onSurfaceCreated() {
                if (isDestroy) {
                    return;
                }
                mTextureView.onShareEglThreadReady(mShareEglThread.getShareContext());
            }

            @Override
            public void onSurfaceChanged(int width, int height) {

            }

            @Override
            public void onDrawFrame() {

            }
        });
        mShareEglThread.start();
    }

    @Override
    public void requestRender() {
        if (mTextureView.getEglThread() != null) {
            mTextureView.getEglThread().requestRender();
        }
    }

    @Override
    public void requestRender(Runnable runnable) {
        if (mTextureView.getEglThread() != null) {
            mTextureView.getEglThread().requestRender(runnable);
        }
    }

    @Override
    public void queueEvent(Runnable runnable) {
        if (mTextureView.getEglThread() != null) {
            mTextureView.getEglThread().queueEvent(runnable);
        }
    }

    @Override
    public void queueEventOnShareContextThread(Runnable runnable) {
        if (mShareEglThread != null) {
            mShareEglThread.queueEvent(runnable);
        }
    }

    @Override
    public void release() {
        isDestroy = true;
        if (mShareEglThread != null) {
            mShareEglThread.onDestroy();
        }
        if (mTextureView != null && mTextureView.getEglThread() != null) {
            mTextureView.getEglThread().onDestroy();
        }
    }
}
