package me.csxiong.camera.opengl;

/**
 * 双缓存FBO。
 */
public class DoubleBuffer {
    // 交替使用的FBO。
    private FBOEntity[] doubleBufferFBO = new FBOEntity[2];
    // 效果需要渲染的Index。
    private int effectDisFBOIndex = 0;

    public DoubleBuffer(int width, int height) {
        // 创建FBO，后续可以改成一个一个创建。
        doubleBufferFBO[0] = TextureHelper.createFBO(width, height);
        doubleBufferFBO[1] = TextureHelper.createFBO(width, height);
    }

    public int getWidth() {
        return doubleBufferFBO[0] != null ? doubleBufferFBO[0].width : 0;
    }

    public int getHeight() {
        return doubleBufferFBO[1] != null ? doubleBufferFBO[1].height : 0;
    }

    /**
     * 交换需要绘制的FBOIndex。
     */
    public void exchangeEffectIndex() {
        effectDisFBOIndex = 1 - effectDisFBOIndex;
    }

    public FBOEntity getDisFBO() {
        return doubleBufferFBO[effectDisFBOIndex];
    }

    @GlThread
    public void release() {
        if (doubleBufferFBO[0] != null) {
            doubleBufferFBO[0].release();
        }
        if (doubleBufferFBO[1] != null) {
            doubleBufferFBO[1].release();
        }
    }
}
