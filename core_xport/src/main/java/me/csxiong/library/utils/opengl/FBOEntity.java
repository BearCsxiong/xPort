package me.csxiong.library.utils.opengl;

import android.opengl.GLES20;

/**
 * FBO与绑定纹理的封装类。
 */
public class FBOEntity {
    public int textureId;
    public int fboId;
    public int width;
    public int height;

    public FBOEntity(int textureId, int fboId, int width, int height) {
        this.textureId = textureId;
        this.fboId = fboId;
        this.width = width;
        this.height = height;
    }

    @GlThread
    public void release() {
        if (fboId == 0 && textureId == 0) {
            return;
        }
        // 原因未知，如果不调用BindFrameBuffer，内存也会一直往上涨。
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glDeleteFramebuffers(1, new int[fboId], 0);
        fboId = 0;
        GLES20.glDeleteTextures(1, new int[] {textureId}, 0);
        textureId = 0;
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public boolean isValid() {
        return textureId != 0 && width > 0 && height > 0;
    }

    /**
     * 判断宽高是否相等。
     * @param fboEntity
     * @return
     */
    public boolean equalSize(FBOEntity fboEntity) {
        if (fboEntity == null) {
            return false;
        }
        return width == fboEntity.width && height == fboEntity.height;
    }

    /**
     * 判断宽高比是否相等。
     * @param fboEntity
     * @return
     */
    public boolean equalSizeAspectRatio(FBOEntity fboEntity) {
        if (fboEntity == null || fboEntity.height == 0 || height == 0) {
            return false;
        }
        return Math.abs(width / (float) height - fboEntity.width / (float) fboEntity.height) < 0.01f;
    }

}
