package me.csxiong.camera.opengl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * 适用于OpenGL渲染的GLTextureView。
 */
public class GLTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    /**
     * GL渲染线程。
     */
    private EglThread mEglThread;
    /**
     * GL渲染环境提供者。
     */
    private TextureViewGLExecutor mGLThreadExecutor;
    /**
     * Studio使用的渲染器。
     */
    private AbsEglRenderer mRenderer;
    /**
     * 手机监听。
     */
    private SurfaceTexture mSurfaceTexture;
    /**
     * 共享GL线程Context。
     */
    private EGLContext mShareEglContext;
    /**
     * Surface的Size。
     */
    private int mSurfaceHeight, mSurfaceWidth;

    public GLTextureView(Context context) {
        this(context, null);
    }

    public GLTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSurfaceTextureListener(this);
        mGLThreadExecutor = new TextureViewGLExecutor();
        mGLThreadExecutor.initGlContext(this);
    }

    public void setRenderer(AbsEglRenderer mRenderer) {
        this.mRenderer = mRenderer;
    }

    public TextureViewGLExecutor getGLThreadExecutor() {
        return mGLThreadExecutor;
    }

    public void onShareEglThreadReady(EGLContext eglContext) {
        mShareEglContext = eglContext;
        initEglThread();
    }

    private void initEglThread() {
        if (mSurfaceTexture == null || mShareEglContext == null) {
            return;
        }
        if (mEglThread == null) {
//            StudioLogUtils.v(EglThread.TAG, "创建EGL线程");
            mEglThread = new EglThread(mShareEglContext);
            mEglThread.setEglRenderer(mRenderer);
            mEglThread.setSurface(mSurfaceTexture, mSurfaceWidth, mSurfaceHeight);
            mEglThread.start();
        } else {
//            StudioLogUtils.v(EglThread.TAG, "重置EGL线程Surface");
            mEglThread.setSurface(mSurfaceTexture, mSurfaceWidth, mSurfaceHeight);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurfaceTexture = surface;
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        initEglThread();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        if (mEglThread != null) {
            mEglThread.setSurface(surface, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public EglThread getEglThread() {
        return mEglThread;
    }

    public void requestRender() {
        if (mEglThread != null) {
            mEglThread.requestRender();
        }
    }

    public void queueEvent(Runnable runnable) {
        if (mEglThread != null) {
            mEglThread.queueEvent(runnable);
        }
    }
}
