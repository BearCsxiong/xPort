package me.csxiong.library.widget;

import android.graphics.drawable.GradientDrawable;

/**
 *  为避免创建过多的资源文件，现建议使用代码创建backgroud 图
 */
public class GradientDrawableFactory {

    public static GradientDrawable createDrawable(int solid){
        return createDrawable(GradientDrawable.RECTANGLE,solid,0,0);
    }

    public static GradientDrawable createDrawable(int solid, float...radius){
        return createDrawable(GradientDrawable.RECTANGLE,solid,0,0,radius);
    }

    public static GradientDrawable createDrawable(int shape, int solid){
        return createDrawable(shape,solid,0,0);
    }

    public static GradientDrawable createDrawable(int shape, int solid, float...radius){
        return createDrawable(shape,solid,0,0,radius);
    }

    public static GradientDrawable createDrawable(int solid, float strokeWidth, int strokeColor){
        return createDrawable(GradientDrawable.RECTANGLE,solid,strokeWidth,strokeColor);
    }

    public static GradientDrawable createDrawable(int shape, int solid, float strokeWidth, int strokeColor){
        return createDrawable(shape,solid,strokeWidth,strokeColor,null);
    }

    public static GradientDrawable createDrawable(int shape, int solid, float strokeWidth, int strokeColor, float...radius){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(solid);
        gradientDrawable.setShape(shape);
        if (strokeWidth>0){
            gradientDrawable.setStroke(DefaultBindAdapter.dip2px(strokeWidth),strokeColor);
        }
        if (radius!=null){
            if (radius.length==1){
                gradientDrawable.setCornerRadius(radius[0]);
            }else {
                gradientDrawable.setCornerRadii(radius);
            }
        }
        return gradientDrawable;
    }

}
