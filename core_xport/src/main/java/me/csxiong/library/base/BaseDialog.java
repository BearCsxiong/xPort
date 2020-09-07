package me.csxiong.library.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Field;

import me.csxiong.library.R;
import me.csxiong.library.utils.ActivityUtils;

/**
 * @Desc : 基础的Dialog
 * @Author : Bear - 2020/9/7
 */
public abstract class BaseDialog<T extends ViewDataBinding> extends BaseDialogFragment {

    protected T mViewBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getDialogStyle());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (getAnimations() != null && window != null) {
            window.setWindowAnimations(getAnimations());
        }
        return dialog;
    }

    protected Integer getAnimations() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), null);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view.getTag(R.id.dataBinding) != null || (view.getTag() != null && view.getTag() instanceof String)) {
            mViewBinding = DataBindingUtil.bind(view);
        }
        bindView();
    }

    protected abstract int getLayoutId();

    protected abstract void bindView();

    public int getDialogStyle() {
        return R.style.BaseDialog;
    }

    public void show() {
        Activity activity = ActivityUtils.getTopActivity();
        if (activity instanceof FragmentActivity) {
            show(((FragmentActivity) activity).getSupportFragmentManager(), this.getClass().getSimpleName());
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        // 复写 该事件避免 IllegalStateException
        Class clz = this.getClass();
        try {
            Field mDismissed = clz.getDeclaredField("mDismissed");
            mDismissed.setAccessible(true);
            mDismissed.set(this, false);
        } catch (NoSuchFieldException e) {
            Log.e(tag, e.toString());
        } catch (IllegalAccessException e) {
            Log.e(tag, e.toString());
        }
        try {
            Field mShownByMe = clz.getDeclaredField("mShownByMe");
            mShownByMe.setAccessible(true);
            mShownByMe.set(this, true);
        } catch (NoSuchFieldException e) {
            Log.e(tag, e.toString());
        } catch (IllegalAccessException e) {
            Log.e(tag, e.toString());
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
