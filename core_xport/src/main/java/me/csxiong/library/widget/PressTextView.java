package me.csxiong.library.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @Desc : 按下透明度0.5的TextView
 * @Author : Bear - 2020/9/7
 */
public class PressTextView extends AppCompatTextView {
    public PressTextView(Context context) {
        super(context);
    }

    public PressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean pressEnable = true;

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (!pressEnable) {
            return;
        }
        if (pressed) {
            setAlpha(0.5f);
        } else {
            setAlpha(1f);
        }
    }

    public void setPressEnable(boolean pressEnable) {
        setAlpha(pressEnable ? 1f : 0.5f);
        setClickable(pressEnable);
        this.pressEnable = pressEnable && isClickable();
    }

    public boolean isPressEnable() {
        return pressEnable;
    }
}
