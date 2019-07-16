package me.csxiong.library.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.trello.rxlifecycle2.android.FragmentEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.csxiong.library.R;
import me.csxiong.library.base.delegate.ViewDelegate;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.integration.lifecycle.IFragmentLifecycle;
import me.csxiong.library.utils.XDisplayUtil;

public abstract class SimpleBottomSheetDialog extends BaseBottomSheetDialogFragment implements IView, IPage, IFragmentLifecycle {

    private final BehaviorSubject<FragmentEvent> mSubject = BehaviorSubject.create();

    private BottomSheetBehavior<FrameLayout> behavior;

    ViewDelegate viewDelegate;

    protected Unbinder unbinder;

    private View view;

    /**
     * fix BottomSheetDialogFragment immersionBar Bug->
     */
    public static class StatusFixSheetDialog extends BottomSheetDialog {

        public StatusFixSheetDialog(@NonNull Context context) {
            super(context);
        }

        public StatusFixSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected StatusFixSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            int screenHeight = XDisplayUtil.getScreenHeight();
            int statusBarHeight = XDisplayUtil.getStatusBarHeight();
            int dialogHeight = screenHeight - statusBarHeight;
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new StatusFixSheetDialog(getContext(), getTheme());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        dialog.setCanceledOnTouchOutside(enableCancelOutside());
        dialog.setCancelable(enableCancelOutside());
        dialog.getWindow().setWindowAnimations(getDialogStyle());
        dialog.getWindow().setGravity(getDialogGravity());
        if (onDismissListener != null) {
            dialog.setOnDismissListener(onDismissListener);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null && behavior == null) {
            bottomSheet.setBackground(getBackground());
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getHeight();
            behavior = BottomSheetBehavior.from(bottomSheet);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheet.getLayoutParams().height = setMaxHeight();
            behavior.setPeekHeight(setPeekHeight());
        }
    }

    private int getHeight() {
        int height = 1920;
        if (getContext() != null) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            if (wm != null) {
                wm.getDefaultDisplay().getSize(point);
                height = point.y - getTopOffset();
            }
        }
        return height;
    }

    public int getTopOffset() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDelegate = new ViewDelegate(this);
        final Window window = getDialog().getWindow();
        view = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        window.setLayout(-1, -1);
        window.setBackgroundDrawable(getBackground());
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initUI(savedInstanceState);
        initData(savedInstanceState);
        return view;
    }

    @Override
    public void onDestroyView() {
        btnClickListener = null;
        onClickListener = null;
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }

    public Drawable getBackground() {
        return new ColorDrawable(Color.TRANSPARENT);
    }

    public boolean enableCancelOutside() {
        return true;
    }

    public int getDialogStyle() {
        return R.style.dialog_sheet_bottom;
    }

    public int getDialogGravity() {
        return Gravity.BOTTOM;
    }

    public int setMaxHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public int setPeekHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
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
    public void dismiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && getActivity() != null && (getActivity().isDestroyed() || getActivity().isFinishing())) {
            return;
        } else if (getActivity() != null && getActivity().isFinishing()) {
            return;
        }
        super.dismiss();
    }

    protected void bind2Btn(View view) {
        view.setOnClickListener(onClickListener);
    }

    protected BtnClickListener btnClickListener;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (btnClickListener != null) {
                btnClickListener.onBtnClick(SimpleBottomSheetDialog.this, view);
            }
        }
    };

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface BtnClickListener {
        void onBtnClick(SimpleBottomSheetDialog dialog, View view);
    }

    public void setOnBtnClickListener(BtnClickListener listener) {
        this.btnClickListener = listener;
    }

    public AppComponent getAppComponent() {
        return APP.get().getAppComponent();
    }

    @Override
    public Subject<FragmentEvent> provideLifecycle() {
        return mSubject;
    }

    public View getRootView() {
        return view;
    }
}
