package me.csxiong.camera.opengl;

/**
 * OPENGL渲染接口，由{@link EglThread}持有，会根据具体的情况调用各个生命周期。
 */
public interface  AbsEglRenderer {

   void onSurfaceCreated();

   void onSurfaceChanged(int width, int height);

   void onDrawFrame();
}
