package me.csxiong.camera.opengl;

/**
 * Gl渲染环境提供者。
 * 可以是{@link android.opengl.GLSurfaceView},也可是客户段自己实现的离屏GL环境。
 */
public class OffScreenGLExecutor implements GLThreadExecutor {
    /**
     * 离屏线程，可用于环境加载。
     */
    private EglThread mShareEglThread;

    public OffScreenGLExecutor() {
        initGlContext();
    }

    /**
     * 初始化GLContext。
     */
    public void initGlContext() {
        mShareEglThread = new EglThread();
        mShareEglThread.start();
    }

    @Override
    public void requestRender() {
        // 离评渲染，这个是没有意义的。
    }

    @Override
    public void requestRender(Runnable runnable) {
        // 离评渲染这两个是等价的。
        queueEvent(runnable);
    }

    @Override
    public void queueEvent(Runnable runnable) {
        if (mShareEglThread != null) {
            mShareEglThread.queueEvent(runnable);
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
        if (mShareEglThread != null) {
            mShareEglThread.onDestroy();
        }
    }
}
