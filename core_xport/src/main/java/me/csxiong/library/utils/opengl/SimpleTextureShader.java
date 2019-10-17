package me.csxiong.library.utils.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glUniform1i;

/**
 * 着色器对象，包含了某组着色器的所有内容。
 * 目前只有一个最简单的着色器，后续如果有其他的在做抽象。
 * 此Shader的作用就是将一个Texture绘制到屏幕中特定的位置。
 */
public class SimpleTextureShader {
    /**
     * 程序id。
     */
    protected int programId = -1;
    /**
     * GL脚本中的变量名称。
     */
    private static final String POSITION = "position";
    private static final String TEXTURE = "texture";
    private static final String TEXCOORD = "texcoord";
    private static final String U_MATRIX = "u_Matrix";
    /**
     * 变量Location缓存。
     */
    protected HashMap<String, Integer> mVaryLocation = new HashMap<>(8);

    public SimpleTextureShader() {
    }

    @GlThread
    public void initOnGl(Context context) {
        if (programId == -1) {
            // TODO: 2018/12/22 这些简单脚本后续改为静态String，而不需要读文件。
//            programId = ShaderHelper.buildProgram(context, R.raw.texture_v, R.raw.texture_f);
            mVaryLocation.put(POSITION, GLES20.glGetAttribLocation(programId, POSITION));
            mVaryLocation.put(TEXTURE, GLES20.glGetUniformLocation(programId, TEXTURE));
            mVaryLocation.put(TEXCOORD, GLES20.glGetAttribLocation(programId, TEXCOORD));
            mVaryLocation.put(U_MATRIX, GLES20.glGetUniformLocation(programId, U_MATRIX));
        }
    }

    /**
     * 绑定脚本所需要的变量。
     *
     * @param textureId
     * @param matrix
     */
    public void bindVertexVary(int textureId, float[] matrix, FloatBuffer glPosition, FloatBuffer texturePosition) {
        GLES20.glUseProgram(programId);
        Integer location = mVaryLocation.get(U_MATRIX);
        if (location != null && location != -1) {
            GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
        }
        location = mVaryLocation.get(POSITION);
        if (location != null && location != -1) {
            glPosition.position(0);
            GLES20.glVertexAttribPointer(location, 2, GLES20.GL_FLOAT, false, 2 * 4, glPosition);
            GLES20.glEnableVertexAttribArray(location);
        }
        location = mVaryLocation.get(TEXCOORD);
        if (location != null && location != -1) {
            texturePosition.position(0);
            GLES20.glVertexAttribPointer(location, 2, GLES20.GL_FLOAT, false, 2 * 4, texturePosition);
            GLES20.glEnableVertexAttribArray(location);
        }
        location = mVaryLocation.get(TEXTURE);
        if (location != null && location != -1) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureId);
            glUniform1i(location, 0);
        }
    }

    /**
     * 绘制顶点。
     */
    public void onDrawFrame() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    /**
     * 将srcFBO中的图片等比复制到disFBO中。
     * @param srcFBO
     * @param disFBO
     */
    @GlThread
    public void copyToFBO(FBOEntity srcFBO, FBOEntity disFBO) {
        // 计算将图片内接于View中间。
        float ratioMax = Math.max(disFBO.width / (float) srcFBO.width, disFBO.height / (float) srcFBO.height);
        float scaleX = srcFBO.width * ratioMax / (float) disFBO.width;
        float scaleY = srcFBO.height * ratioMax / (float) disFBO.height;
        // 通过改顶点坐标来设置大小。
        float[] texVertex = VertexHelper.TEXTURE_VERTEX_FLOATS;
        float offsetX = (1 - 1 / scaleX) / 2;
        float offsetY = (1 - 1 / scaleY) / 2;
        texVertex = new float[] {texVertex[0] / scaleX + offsetX, texVertex[1] / scaleY + offsetY,
            texVertex[2] / scaleX + offsetX, texVertex[3] / scaleY + offsetY, texVertex[4] / scaleX + offsetX,
            texVertex[5] / scaleY + offsetY, texVertex[6] / scaleX + offsetX, texVertex[7] / scaleY + offsetY,};
        // 顶点数组。
        FloatBuffer glPosition =
            ByteBuffer.allocateDirect(VertexHelper.GL_VERTEX_FLOATS_IN_FBO.length * VertexHelper.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VertexHelper.GL_VERTEX_FLOATS_IN_FBO);
        // 纹理顶点。
        FloatBuffer texturePosition =
            ByteBuffer.allocateDirect( texVertex.length * VertexHelper.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put( texVertex);
        // 单位矩阵。
        float[] matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        // 设置变量。
        bindVertexVary(srcFBO.textureId, matrix, glPosition, texturePosition);
        // 绘制到FBO上。
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, disFBO.fboId);
        GLES20.glViewport(0, 0, disFBO.width, disFBO.height);
        onDrawFrame();
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    @GlThread
    public void copyToScreen(FBOEntity disFBO) {
        // 顶点数组。
        FloatBuffer glPosition =
            ByteBuffer.allocateDirect(VertexHelper.GL_VERTEX_FLOATS.length * VertexHelper.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VertexHelper.GL_VERTEX_FLOATS);
        // 纹理顶点。
        FloatBuffer texturePosition =
            ByteBuffer.allocateDirect(VertexHelper.TEXTURE_VERTEX_FLOATS.length * VertexHelper.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VertexHelper.TEXTURE_VERTEX_FLOATS);
        // 单位矩阵。
        float[] matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        // 设置变量。
        bindVertexVary(disFBO.textureId, matrix, glPosition, texturePosition);
        // 绘制到FBO上。
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glViewport(0, 0, disFBO.width, disFBO.height);
        onDrawFrame();
    }
}
