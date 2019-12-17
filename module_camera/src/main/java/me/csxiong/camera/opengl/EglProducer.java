package me.csxiong.camera.opengl;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLUtils;
import android.support.annotation.NonNull;

import javax.microedition.khronos.egl.EGL10;

/**
 * Egl环境持有者。Egl的创建、销毁、刷新显存Buffer都由它负责。
 */
public class EglProducer {

    private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    private static final int EGL_OPENGL_ES2_BIT = 4;

    /**
     * Egl屏幕对象。
     */
    private EGLDisplay mEglDisplay = EGL14.EGL_NO_DISPLAY;
    /**
     * Egl的显存，绘制的数据会存在显存里，调用swap才会渲染到EGLDisplay中。
     */
    private EGLSurface mEglSurface = EGL14.EGL_NO_SURFACE;
    /**
     * Egl环境, 附属于EGLDisplay。
     */
    private EGLContext mEglContext = EGL14.EGL_NO_CONTEXT;

    private EGLConfig mEglConfig;

    /**
     * 初始化EGL环境，底下是标准的EGL初始化流程，具体的内容我也是一知半解。http://cf.meitu.com/confluence/pages/viewpage.action?pageId=55552960
     * @param shareContext 共享Egl环境，可以跨线程访问GL资源 ，不需要时传EGL10.EGL_NO_CONTEXT。
     */
    public void createEGL(@NonNull EGLContext shareContext) {

        // 获取一块"虚拟显示屏"。
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetdisplay failed : " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        }

        int[] version = new int[2];
        if (!EGL14.eglInitialize(mEglDisplay, version, 0, version, 1)) {
            throw new RuntimeException("eglInitialize failed : " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        }

        // 配置EGL的颜色空间深度空间等信息。
        int[] configAttributes = {EGL14.EGL_BUFFER_SIZE, 32, EGL14.EGL_ALPHA_SIZE, 8, EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8, EGL14.EGL_RED_SIZE, 8, EGL14.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT, EGL14.EGL_NONE};

        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        if (!EGL14.eglChooseConfig(mEglDisplay, configAttributes, 0, configs, 0, configs.length, numConfigs, 0)) {
            throw new RuntimeException("eglChooseConfig failed : " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        }
        mEglConfig = configs[0];

        // 根据上面的Display配置创建EGL环境。
        int[] contextAttributes = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE};
        mEglContext = EGL14.eglCreateContext(mEglDisplay, mEglConfig, shareContext, contextAttributes, 0);

        // 读取纹理最大尺寸
    }

    /**
     * 创建EGLSurface。在初始化和Surface发生变化的时候都要调用。
     * @param surface {@link android.view.Surface} 或者是 {@link android.graphics.SurfaceTexture}.
     */
    public void createSurface(Object surface) {
        // 检测状态。
        if (mEglDisplay == null) {
            throw new RuntimeException("eglDisplay not initialized");
        }
        if (mEglConfig == null) {
            throw new RuntimeException("mEglConfig not initialized");
        }
        // 销毁之前的Surface。
        destroyEGLSurface();
        // 创建EGL的"显存"，对于没有Surface的离屏渲染需要使用我们特别的SurfaceFactory。
        if (surface == null) {
            mEglSurface = new NoWindowSurfaceFactory().createWindowSurface(mEglDisplay, mEglConfig);
        } else {
            int[] surfaceAttributes = {EGL14.EGL_NONE};
            mEglSurface = EGL14.eglCreateWindowSurface(mEglDisplay, mEglConfig, surface, surfaceAttributes, 0);
        }
        if (mEglSurface == EGL14.EGL_NO_SURFACE || mEglContext == EGL14.EGL_NO_CONTEXT) {
            int error = EGL14.eglGetError();
            if (error == EGL10.EGL_SUCCESS) {
                // 界面退出的时候有时候会出现这个问题，暂时先这么解决。
                return;
            }
            if (error == EGL14.EGL_BAD_NATIVE_WINDOW) {
                throw new RuntimeException("eglCreateWindowSurface returned  EGL_BAD_NATIVE_WINDOW. ");
            }
            throw new RuntimeException("eglCreateWindowSurface failed : " + GLUtils.getEGLErrorString(error));
        }

        if (!EGL14.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
            throw new RuntimeException("eglMakeCurrent failed : " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        }
    }

    /**
     * 销毁当前的EGL环境。
     */
    private void destroyEGLSurface() {
        if (mEglSurface != null && mEglSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglMakeCurrent(mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(mEglDisplay, mEglSurface);
            mEglSurface = null;
        }
    }

    /**
     * 获取当前的Egl环境，用于共享。
     * @return
     */
    public EGLContext getEglContext() {
        return mEglContext;
    }

    /**
     * 销毁EGL。
     */
    public void destroyEGL() {
        destroyEGLSurface();
        EGL14.eglDestroyContext(mEglDisplay, mEglContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(mEglDisplay);
        mEglDisplay = EGL14.EGL_NO_DISPLAY;
        mEglContext = EGL14.EGL_NO_CONTEXT;
        mEglConfig = null;
    }

    /**
     * 刷新界面，每次绘制后需要调用一次这个才会改变显示内容。
     */
    public void refreshSurface() {
        if (mEglSurface != null) {
            EGL14.eglSwapBuffers(mEglDisplay, mEglSurface);
        }
    }

    /**
     * 没有Surface Object传入的EGLSurface创建工厂，适用于没有Surface的离屏渲染。
     */
    private class NoWindowSurfaceFactory {
        /**
         * 设置虚拟Surface的宽度、高度，具体数值不确定由什么影响，随便设置了一个10。
         */
        private static final int DEFAULT_SIZE = 10;

        public EGLSurface createWindowSurface(EGLDisplay display, EGLConfig config) {
            int[] attribList =
                new int[] {EGL14.EGL_WIDTH, DEFAULT_SIZE, EGL14.EGL_HEIGHT, DEFAULT_SIZE, EGL14.EGL_NONE};
            return EGL14.eglCreatePbufferSurface(display, config, attribList, 0);
        }
    }
}
