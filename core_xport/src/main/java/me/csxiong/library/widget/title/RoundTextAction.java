package me.csxiong.library.widget.title;

import android.content.Context;

import me.csxiong.library.utils.XDisplayUtil;
import me.csxiong.library.widget.round.RoundTextView;


/**
 * @Desc : 部分RoundTextview Action
 * @Author : csxiong - 2019-08-27
 */
public abstract class RoundTextAction extends Action<RoundTextView> {

    private RoundTextView mTv;

    @Override
    RoundTextView onCreateAction(Context context) {
        if (mTv == null) {
            mTv = new RoundTextView(context);
            onConvertRoundView(mTv);
        }
        return mTv;
    }

    protected abstract void onConvertRoundView(RoundTextView rtv);

    @Override
    public int[] marginLTRB() {
        return new int[]{XDisplayUtil.dpToPxInt(16), XDisplayUtil.dpToPxInt(3), XDisplayUtil.dpToPxInt(16), XDisplayUtil.dpToPxInt(3)};
    }
}
