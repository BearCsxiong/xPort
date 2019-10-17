package me.csxiong.library.utils.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import me.csxiong.library.base.APP;

import static android.opengl.GLES20.GL_TEXTURE_2D;

/**
 * OpenGl帮助类。
 */
public class TextureHelper {

    /**
     * 根据一张图片创建一个纹理并绑定到FBO上。
     *
     * @param bitmap 图片
     * @return 纹理ID与FBO ID。
     */
    @GlThread
    public static FBOEntity createFboWithImg(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        // 创建纹理单元。
        int[] textures = generateTexture();
        // 将图片转换到纹理上。
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        // 创建FBO。
        int[] fbo = generateAndBindFbo(textures);
        return new FBOEntity(textures[0], fbo[0], bitmap.getWidth(), bitmap.getHeight());
    }

    /**
     * 创建一块空的纹理绑定FBO。
     *
     * @param width
     * @param height
     * @return
     */
    @GlThread
    public static FBOEntity createFBO(int width, int height) {
        // 创建纹理单元。
        int[] textures = generateTexture();
        // 为Texture创建一块内存。
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE, null);
        // 创建FBO。
        int[] fbo = generateAndBindFbo(textures);
        return new FBOEntity(textures[0], fbo[0], width, height);
    }

    @GlThread
    public static FBOEntity copyFBOWithSameTexture(FBOEntity fboEntity) {
        int[] textures = new int[] {fboEntity.textureId};
        int[] fbo = generateAndBindFbo(textures);
        return new FBOEntity(textures[0], fbo[0], fboEntity.width, fboEntity.height);
    }

    /**
     * 创建用于接收相机输出帧的Texture。
     * @return
     */
    @GlThread
    public static int createOESTexture() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return texture[0];
    }

    /**
     * 生成TextureId。
     *
     * @return
     */
    @GlThread
    private static int[] generateTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        // 更改状态，设置纹理属性。
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        // 设置纹理参数。
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        return textures;
    }

    /**
     * 生成并绑定纹理的Fbo。
     *
     * @param textures
     * @return
     */
    @GlThread
    private static int[] generateAndBindFbo(int[] textures) {
        int[] fbo = new int[1];
        GLES20.glGenFramebuffers(fbo.length, fbo, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
            textures[0], 0);

        int fboStatus = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if (fboStatus != GLES20.GL_FRAMEBUFFER_COMPLETE) {
//            Debug.e("TextureHelper", "initFBO failed, status: " + fboStatus);
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return fbo;
    }

    /**
     * 从制定FBO中读取一张图片。
     *
     * @param fbo
     * @param textureWidth
     * @param textureHeight
     * @return
     */
    @GlThread
    public static Bitmap loadBitmapFromFbo(int fbo, int textureWidth, int textureHeight) {
        if (textureWidth == 0 || textureHeight == 0) {
            return null;
        }
        // 不太明白这个函数的作用。。
        GLES20.glFinish();
        // 创建图片内存。
        int bitMapBufferSize = textureWidth * textureHeight * 4;
        ByteBuffer buf = ByteBuffer.allocateDirect(bitMapBufferSize);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.rewind();
        buf.position(0);
        // FBO中读tu。
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo);
        GLES20.glReadPixels(0, 0, textureWidth, textureHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buf);
        Bitmap outputBitmap = Bitmap.createBitmap(textureWidth, textureHeight, Bitmap.Config.ARGB_8888);
        outputBitmap.copyPixelsFromBuffer(buf);
        buf.position(0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return outputBitmap;
    }

    /**
     * 从制定FBO中读取一张图片。
     *
     * @param fboEntity
     * @return
     */
    public static Bitmap loadBitmapFromFbo(@NonNull FBOEntity fboEntity) {
        return loadBitmapFromFbo(fboEntity.fboId, fboEntity.width, fboEntity.height);
    }

    /**
     * 拷贝Texture，生成一个新的FBO。
     * @param srcFBO
     * @return
     */
    public static FBOEntity copyFBO(FBOEntity srcFBO) {
        SimpleTextureShader copyTextureShader = new SimpleTextureShader();
        copyTextureShader.initOnGl(APP.get());
        FBOEntity disFBO = TextureHelper.createFBO(srcFBO.width, srcFBO.height);
        copyTextureShader.copyToFBO(srcFBO, disFBO);
        return disFBO;
    }

    public static void copyFBO(FBOEntity srcFBO, FBOEntity disFBO) {
        SimpleTextureShader copyTextureShader = new SimpleTextureShader();
        copyTextureShader.initOnGl(APP.get());
        copyTextureShader.copyToFBO(srcFBO, disFBO);
    }
}
