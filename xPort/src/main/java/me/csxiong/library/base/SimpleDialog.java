package me.csxiong.library.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.csxiong.library.R;
import me.csxiong.library.base.delegate.ViewDelegate;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.integration.lifecycle.IFragmentLifecycle;

/**
 * Created by csxiong on 2018/11/2.
 */

public abstract class SimpleDialog extends BaseDialogFragment implements IView, IPage, IFragmentLifecycle {

    private final BehaviorSubject<FragmentEvent> mSubject = BehaviorSubject.create();

    protected Unbinder unbinder;

    ViewDelegate viewDelegate;

    private View rootView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(enableCancelOutside());
        dialog.setCancelable(enableCancelOutside());
        dialog.getWindow().setWindowAnimations(getDialogStyle());
        dialog.getWindow().setGravity(getDialogGravity());
        if (onDismissListener != null) {
            dialog.setOnDismissListener(onDismissListener);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDelegate = new ViewDelegate(this);
        final Window window = getDialog().getWindow();
        rootView = inflater.inflate(getLayoutResId(), window.findViewById(android.R.id.content), false);
        rootView.setBackground(getBackground());
        unbinder = ButterKnife.bind(this, rootView);
        window.setLayout(-1, -2);
        window.setBackgroundDrawable(getBackground());
        initUI(savedInstanceState);
        initData(savedInstanceState);
        ImmersionBar.with(this).init();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        btnClickListener = null;
        onClickListener = null;
        ImmersionBar.with(this).destroy();
        super.onDestroyView();
    }

    public Drawable getBackground() {
        return new ColorDrawable(Color.TRANSPARENT);
    }

    public boolean enableCancelOutside() {
        return true;
    }

    public int getDialogStyle() {
        return R.style.dialog_transition;
    }

    public int getDialogGravity() {
        return Gravity.CENTER;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && getActivity() != null && (getActivity().isDestroyed() || getActivity().isFinishing())) {
            return;
        } else if (getActivity() != null && getActivity().isFinishing()) {
            return;
        }
        super.show(manager, tag);
    }

    @Override
    public void startLoading(String msg) {
        if (viewDelegate != null) {
            viewDelegate.startLoading(msg);
        }
    }

    @Override
    public void stopLoading() {
        if (viewDelegate != null) {
            viewDelegate.stopLoading();
        }
    }

    protected void bind2Btn(View view) {
        view.setOnClickListener(onClickListener);
    }

    protected BtnClickListener btnClickListener;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (btnClickListener != null) {
                btnClickListener.onBtnClick(SimpleDialog.this, view);
            }
        }
    };

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface BtnClickListener {
        void onBtnClick(SimpleDialog dialog, View view);
    }

    public void setOnBtnClickListener(BtnClickListener listener) {
        this.btnClickListener = listener;
    }

    public AppComponent getAppComponent() {
        return APP.getInstance().getAppComponent();
    }

    @Override
    public Subject<FragmentEvent> provideLifecycle() {
        return mSubject;
    }

    public View getRootView() {
        return rootView;
    }
}
