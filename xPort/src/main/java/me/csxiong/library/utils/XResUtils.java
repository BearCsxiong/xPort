package me.csxiong.library.utils;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import me.csxiong.library.base.APP;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : 资源数据获取
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/4/10 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class XResUtils {

    public static Animation getAnim(int animResId, Animation.AnimationListener listener) {
        Animation animation = AnimationUtils.loadAnimation(APP.getInstance(), animResId);
        if (listener != null) {
            animation.setAnimationListener(listener);
        }
        return animation;
    }

    public static Animation getAnim(int animResId) {
        return getAnim(animResId, null);
    }

    public static String getString(int stringResId) {
        return APP.getInstance().getResources().getString(stringResId);
    }

    public static int getColor(int colorResId) {
        return ContextCompat.getColor(APP.getInstance(), colorResId);
    }

    public static int getColor(String color) {
        return Color.parseColor(color);
    }

    public static float getDimen(int dimensResId) {
        return APP.getInstance().getResources().getDimension(dimensResId);
    }

    public static int getDimenInt(int dimensResId) {
        return (int) getDimen(dimensResId);
    }

    public static int getDimenPx(int dimensResId) {
        return (int) XDisplayUtil.dpToPxInt(getDimen(dimensResId));
    }

    public static float getDimen(String dimenName) {
        return getDimen(getResIdByName(dimenName, "dimen"));
    }

    public static int getResIdByName(String resName, String resType) {
        return APP.getInstance().getResources().getIdentifier(resName, resType, APP.getInstance().getPackageName());
    }

    public static Drawable getDrawable(int drawableResId) {
        Drawable drawable = ContextCompat.getDrawable(APP.getInstance(), drawableResId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public static String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = APP.getInstance().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //获取适配屏幕内容
    public static float getXDimen(int px) {
        return getDimen("x" + px);
    }

    public static float getAttrValue(int styleAttr) {
        TypedValue typedValue = new TypedValue();
        APP.getInstance().getTheme().resolveAttribute(styleAttr, typedValue, true);
        return typedValue.getFloat();
    }

    public abstract static class AnimationStartListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public abstract static class AnimationEndListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public abstract static class AnimationRepeatListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {

        }
    }

}
