package me.csxiong.camera.opengl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * Created by meitu on 2016/7/11.
 * 定义了绘制纹理到帧缓冲区的接口
 */
public class TextureProgram {

    private static final String TAG = "FLY_TextureProgram";


    public static final int TEX_OES = 0;
    public static final int TEX_2D = 1;
    public static final int TEX_I420 = 2;
    public static final int TEX_NV21 = 3;
    public static final int TEX_2D_GRAY = 4;
    public static final int TEX_2D_ALPHA = 5;

    public TextureProgram(int textureType) {
        String fss;
        switch (textureType) {
            case TEX_2D:
                fss = fss2D;
                break;
            case TEX_OES:
                fss = fssOES;
                break;
            case TEX_2D_GRAY:
                fss = fss2DGray;
                break;
            case TEX_I420:
                fss = fssI420;
                break;
            case TEX_NV21:
                fss = fssNV21;
                break;
            case TEX_2D_ALPHA:
                fss = fss2DAlpha;
                break;
            default:
                throw new RuntimeException("error texture type");
        }
        mProgramHandle = ShaderHelper.buildProgram(vss, fss);
        maPositionLoc = GLES20.glGetAttribLocation(mProgramHandle, "vPosition");
        maTextureCoordLoc = GLES20.glGetAttribLocation(mProgramHandle, "vTexCoord");
        if (textureType == TEX_I420) {
            mTexUniformLoc = new int[3];
            mTexUniformLoc[0] = GLES20.glGetUniformLocation(mProgramHandle, "yTexture");
            mTexUniformLoc[1] = GLES20.glGetUniformLocation(mProgramHandle, "uTexture");
            mTexUniformLoc[2] = GLES20.glGetUniformLocation(mProgramHandle, "vTexture");
        } else if (textureType == TEX_NV21) {
            mTexUniformLoc = new int[2];
            mTexUniformLoc[0] = GLES20.glGetUniformLocation(mProgramHandle, "yTexture");
            mTexUniformLoc[1] = GLES20.glGetUniformLocation(mProgramHandle, "uvTexture");
        } else {
            mTexUniformLoc = new int[1];
            mTexUniformLoc[0] = GLES20.glGetUniformLocation(mProgramHandle, "sTexture");
        }
        mMatUniformLoc = GLES20.glGetUniformLocation(mProgramHandle, "matrix");
        mTexMatUniformLoc = GLES20.glGetUniformLocation(mProgramHandle, "texMatrix");
    }

    public void draw(FloatBuffer vertexBuffer, FloatBuffer texBuffer, int[] textureIds, int texTarget, int fbo, float[] vertexMatrix, float[] texMatrix) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo);
        GLES20.glUseProgram(mProgramHandle);

        for (int i = 0; i < textureIds.length; ++i) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glUniform1i(mTexUniformLoc[i], i);
            GLES20.glBindTexture(texTarget, textureIds[i]);
        }

        GLES20.glUniformMatrix2fv(mMatUniformLoc, 1, false, vertexMatrix, 0);
        GLES20.glUniformMatrix4fv(mTexMatUniformLoc, 1, false, texMatrix, 0);


        GLES20.glEnableVertexAttribArray(maPositionLoc);
        GLES20.glVertexAttribPointer(maPositionLoc, 2,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(maTextureCoordLoc);
        GLES20.glVertexAttribPointer(maTextureCoordLoc, 2,
                GLES20.GL_FLOAT, false, 0, texBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //GLES20.glDisableVertexAttribArray(maPositionLoc);
        //GLES20.glDisableVertexAttribArray(maTextureCoordLoc);

    }

    public void release() {
        if (mProgramHandle != 0) {
            GLES20.glDeleteProgram(mProgramHandle);
            mProgramHandle = 0;
        }
    }

    private int mProgramHandle;
    private int maPositionLoc;
    private int maTextureCoordLoc;
    private int[] mTexUniformLoc;
    private int mMatUniformLoc;
    private int mTexMatUniformLoc;

    private static final String vss = ""
            + "uniform mat2 matrix;"
            + "uniform mat4 texMatrix;"
            + "attribute vec2 vPosition;\n"
            + "attribute vec2 vTexCoord;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  texCoord = (texMatrix * vec4(vTexCoord,0,1)).st;\n"
            + "  gl_Position = vec4 ( matrix * vPosition, 0.0, 1.0 );\n"
            + "}";
    private static final String fssOES = ""
            + "#extension GL_OES_EGL_image_external : require\n"
            + "precision mediump float;\n"
            + "uniform samplerExternalOES sTexture;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  gl_FragColor = texture2D(sTexture,texCoord);\n"
            + "}";
    private final String fss2D = ""
            + "precision mediump float;\n"
            + "uniform sampler2D sTexture;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  gl_FragColor = vec4(texture2D(sTexture,texCoord).rgb,1);\n"
            + "}";
    private final String fss2DAlpha = ""
            + "precision mediump float;\n"
            + "uniform sampler2D sTexture;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  gl_FragColor = texture2D(sTexture,texCoord);\n"
            + "}";
    private final String fss2DGray = ""
            + "precision mediump float;\n"
            + "uniform sampler2D sTexture;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  vec3 texColor = texture2D(sTexture,texCoord).rgb;\n"
            + "  float i = (texColor.r + texColor.g + texColor.b) / 3.0;"
            + "  gl_FragColor = vec4(1,0,0,1);\n" + "}";
    private final String fssI420 = ""
            + "precision mediump float;\n"
            + "uniform sampler2D yTexture;\n"
            + "uniform sampler2D uTexture;\n"
            + "uniform sampler2D vTexture;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  float y = texture2D(yTexture,texCoord).r;\n"
            + "  float u = texture2D(uTexture,texCoord).r - 0.5;\n"
            + "  float v = texture2D(vTexture,texCoord).r - 0.5;\n"
            + "  vec3 rgb = mat3(1,1,1,0,-0.34413,1.772,1.402,-0.71414,0) * vec3(y,u,v);\n"
            + "  gl_FragColor = vec4(rgb,1);\n"
            + "}";
    private final String fssNV21 = ""
            + "precision mediump float;\n"
            + "uniform sampler2D yTexture;\n"
            + "uniform sampler2D uvTexture;\n"
            + "varying vec2 texCoord;\n"
            + "void main() {\n"
            + "  float y = texture2D(yTexture,texCoord).r;\n"
            + "  float v = texture2D(uvTexture,texCoord).r - 0.5;\n"
            + "  float u = texture2D(uvTexture,texCoord).a - 0.5;\n"
            + "  vec3 rgb = mat3(1,1,1,0,-0.34413,1.772,1.402,-0.71414,0) * vec3(y,u,v);\n"
            + "  gl_FragColor = vec4(rgb,1);\n"
            + "}";
}
