package me.csxiong.library.widget.title;

import android.content.Context;
import android.view.View;

/**
 * Titlebar上面Action添加
 */
public abstract class Action<T extends View> {

    abstract T onCreateAction(Context context);

    public abstract void onActionClick(View view);

    public abstract int[] marginLTRB();

}
