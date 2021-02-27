package me.csxiong.library.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.IdRes
import com.commsource.util.ResourcesUtils

/**
 * @Desc : 包装动画图片的一个计算类
 * @Author : Bear - 2/7/21
 *
 * 一个原则是 XAnimatorDrawable以图片为中心 绘制
 */
class XAnimatorDrawable {

    constructor(drawableId: Int) {
        targetDrawable = ResourcesUtils.getDrawable(drawableId)
    }

    constructor(drawable: Drawable?) {
        targetDrawable = drawable
    }

    /**
     * 目标动画drawable对象
     */
    var targetDrawable: Drawable? = null

    /**
     * 缩放Valuer
     */
    val zoomValuer = XAnimatorCaculateValuer(1f)

    /**
     * 位移XValuer
     */
    val translationXValuer = XAnimatorCaculateValuer(0f)

    /**
     * 位移YValuer
     */
    val translationYValuer = XAnimatorCaculateValuer(0f)

    /**
     * 透明度valuer
     */
    val alphaValuer = XAnimatorCaculateValuer(1f)

    /**
     * 宽度值
     */
    val widthValuer = XAnimatorCaculateValuer(0f)

    /**
     * 高度值
     */
    val heightValuer = XAnimatorCaculateValuer(0f)

    fun width(width: Float): XAnimatorDrawable {
        widthValuer.value = width
        return this
    }

    fun height(height: Float): XAnimatorDrawable {
        heightValuer.value = height
        return this
    }

    fun translationX(translateX: Float): XAnimatorDrawable {
        translationXValuer.value = translateX
        return this
    }

    fun translationY(translateY: Float): XAnimatorDrawable {
        translationYValuer.value = translateY
        return this
    }

    fun alpha(alpha: Float): XAnimatorDrawable {
        alphaValuer.value = alpha
        return this
    }

    fun zoom(zoom: Float): XAnimatorDrawable {
        zoomValuer.value = zoom
        return this
    }

    fun init(): XAnimatorDrawable {
        //其实就是value计算一遍
        caculateFraction(1f)
        return this
    }

    fun toZoom(endZoom: Float): XAnimatorDrawable {
        zoomValuer.to(endZoom)
        return this
    }

    fun toAlpha(endAlpha: Float): XAnimatorDrawable {
        alphaValuer.to(endAlpha)
        return this
    }

    fun toTranslationX(endTranslationX: Float): XAnimatorDrawable {
        translationXValuer.to(endTranslationX)
        return this
    }

    fun toTranslationY(endTranslationY: Float): XAnimatorDrawable {
        translationYValuer.to(endTranslationY)
        return this
    }

    fun caculateFraction(fraction: Float) {
        val zoom = zoomValuer.caculateValue(fraction)
        val halfHeight = (heightValuer.caculateValue(fraction) * zoom / 2f).toInt()
        val halfWidth = (widthValuer.caculateValue(fraction) * zoom / 2f).toInt()
        val translationX = translationXValuer.caculateValue(fraction).toInt()
        val translationY = translationYValuer.caculateValue(fraction).toInt()
        val alpha = alphaValuer.caculateValue(fraction)
        targetDrawable?.alpha = (XAnimatorCaculateValuer.limit(255 * alpha, 0f, 255f)).toInt()
        targetDrawable?.setBounds(-halfWidth, -halfHeight, halfWidth, halfHeight)
        targetDrawable?.bounds?.offset(translationX, translationY)
    }

    fun draw(canvas: Canvas) {
        if (targetDrawable?.bounds?.width() ?: 0 > 0 && targetDrawable?.bounds?.height() ?: 0 > 0) {
            targetDrawable?.draw(canvas)
        }
    }

}