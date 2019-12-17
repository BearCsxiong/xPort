package me.csxiong.camera.opengl;

/**
 * Gl渲染环境提供者。
 * 可以是{@link android.opengl.GLSurfaceView},也可是客户段自己实现的离屏GL环境。
 */
public interface GLThreadExecutor {
    /**
     * 请求渲染。
     */
    void requestRender();

    /**
     * 请求渲染。
     */
    void requestRender(Runnable runnable);

    /**
     * 在GL线程执行。
     *
     * @param runnable
     */
    void queueEvent(Runnable runnable);

    /**
     * 在共享线程中执行。
     * @param runnable
     */
    void queueEventOnShareContextThread(Runnable runnable);

    /**
     * 释放资源。
     */
    void release();
}
