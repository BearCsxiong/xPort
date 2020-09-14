package me.csxiong.library.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import me.csxiong.library.R

/**
 * @Desc : 修改字号描边
 * @Author : Bear - 2020/9/14
 */
class BoldTextView(context: Context, attributeSet: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    var strokeWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.BoldTextView)
        strokeWidth = array.getFloat(R.styleable.BoldTextView_boldTextWidth, 0.8f)
        array.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = strokeWidth
        super.onDraw(canvas)
    }
}