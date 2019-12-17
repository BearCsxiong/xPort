package me.csxiong.camera.opengl;

/**
 * 顶点帮助类。
 */
public class VertexHelper {
    /**
     * 每个float占用4个字节。
     */
    public static int BYTES_PER_FLOAT = 4;
    /**
     * GL脚本的顶点数组，左下、右下、左上、右上4个顶点。
     * GL_TRIANGLE_STRIP的绘制方式详见：https://blog.csdn.net/xiajun07061225/article/details/7455283。
     */
    public static final float[] GL_VERTEX_FLOATS = {
            -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f,};
    /**
     * 纹理顶点数组，左下、右下、左上、右上4个顶点。
     */
    public static final float[] TEXTURE_VERTEX_FLOATS = {
            0f, 1f, 1.0f, 1f, 0f, 0f, 1f, 0f,};
    /**
     * 纹理顶点数组，左下、右下、左上、右上4个顶点。
     */
    public static final float[] GL_VERTEX_FLOATS_IN_FBO = {
            -1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f,};
}
