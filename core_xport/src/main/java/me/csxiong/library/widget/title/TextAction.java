package me.csxiong.library.widget.title;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.widget.TextView;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 文字Action
 * @Author : csxiong - 2019-08-27
 */
public abstract class TextAction extends Action<TextView> {

    private String actionStr;

    private TextView tv;

    public TextAction(String actionStr) {
        this.actionStr = actionStr;
    }

    @SuppressLint("ResourceType")
    @Override
    public TextView onCreateAction(Context context) {
        if (tv == null) {
            tv = new TextView(context);
            tv.setText(actionStr);
            tv.setTextColor(0xfff25c7f);
            TypedValue typedValue = new TypedValue();
            int[] attribute = new int[]{android.R.attr.actionBarItemBackground};
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
            tv.setBackground(typedArray.getDrawable(0));
            tv.setPadding(XDisplayUtil.dpToPxInt(16), XDisplayUtil.dpToPxInt(3), XDisplayUtil.dpToPxInt(16), XDisplayUtil.dpToPxInt(3));
//            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        }
        return tv;
    }

    @Override
    public int[] marginLTRB() {
        return new int[]{0, 0, 0, 0};
    }
}
