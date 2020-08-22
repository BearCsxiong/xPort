package me.csxiong.library.widget.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.widget.ImageView;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 图片Action
 * @Author : csxiong - 2019-08-27
 */
public abstract class ImageAction extends Action<ImageView> {

    private int imageResource;

    private ImageView mIv;

    public ImageAction(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public ImageView onCreateAction(Context context) {
        if (mIv == null) {
            mIv = new ImageView(context);
            TypedValue typedValue = new TypedValue();
            int[] attribute = new int[]{android.R.attr.actionBarItemBackground};
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
            mIv.setBackground(typedArray.getDrawable(0));
            mIv.setPadding(XDisplayUtil.dpToPxInt(16), XDisplayUtil.dpToPxInt(3), XDisplayUtil.dpToPxInt(16), XDisplayUtil.dpToPxInt(3));
            mIv.setImageResource(imageResource);
        }
        return mIv;
    }

    @Override
    public int[] marginLTRB() {
        return new int[]{0, 0, 0, 0};
    }
}
