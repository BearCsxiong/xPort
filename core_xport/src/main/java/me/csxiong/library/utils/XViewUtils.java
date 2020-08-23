package me.csxiong.library.utils;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorLong;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Desc : View工具
 * @Author : Bear - 2020/8/23
 */
public class XViewUtils {

    /**
     * 将当前View从其父View中移出。
     *
     * @param view
     */
    public static void removeFromParent(View view) {
        if (view == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }

    /**
     * 往Activity上贴一个Fragment。
     *
     * @return 是否第一次贴上
     */
    public static boolean attachFragmentToActivity(Fragment fragment, int layoutId, String tag,
                                                   FragmentManager fragmentManager) {
        if (fragment == null || fragmentManager == null) {
            return false;
        }
        if (fragment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment).commitNowAllowingStateLoss();
            return false;
        } else {
            fragmentManager.beginTransaction().add(layoutId, fragment, tag).commitNowAllowingStateLoss();
            return true;
        }
    }

    @UiThread
    public static void setHeight(View view, int height) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    @UiThread
    public static void setWidth(View view, int width) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
        }
    }

    @UiThread
    public static void setMarginTop(View view, int top) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = top;
            view.setLayoutParams(layoutParams);
        }
    }

    @UiThread
    public static void setMarginBottom(View view, int bottom) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = bottom;
            view.setLayoutParams(layoutParams);
        }
    }

    @UiThread
    public static void setMarginLeft(View view, int left) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = left;
            view.setLayoutParams(layoutParams);
        }
    }

    public static int getMarginLeft(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
        }
        return 0;
    }

    @UiThread
    public static void setMarginRight(View view, int right) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = right;
            view.setLayoutParams(layoutParams);
        }
    }

    public static void setTextSpanColor(SpannableString spanString, String childString, @ColorLong int color) {
        String string = spanString.toString();
        int indexStart = string.indexOf(childString);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        if (indexStart >= 0) {
            spanString.setSpan(colorSpan, indexStart, indexStart + childString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), indexStart, indexStart + childString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static SpannableString toSingleColorSpanString(String total, String child, @ColorLong int color) {
        SpannableString spannableString = new SpannableString(total);
        setTextSpanColor(spannableString, child, color);
        return spannableString;
    }

    public static void show(View view) {
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hide(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    public static void setInVisible(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void setVisibilitySafety(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public static void showOrHideView(View view, boolean show) {
        if (view == null) {
            return;
        }
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 更改View 状态。
     *
     * @param view
     * @param enable
     */
    public static void updateViewState(View view, boolean enable) {
        if (enable) {
            view.setAlpha(1f);
            view.setEnabled(true);
            view.setClickable(true);
        } else {
            view.setAlpha(0.3f);
            view.setEnabled(false);
            view.setClickable(false);
        }
    }

    public static void alphaDismiss(View view, long duration) {
        if (view == null) {
            return;
        }
        view.animate().cancel();
        view.animate().setDuration(duration).alpha(0).start();
    }

    public static void alphaShow(View view, long duration) {
        if (view == null) {
            return;
        }
        view.animate().cancel();
        view.animate().setDuration(duration).alpha(1).start();
    }

    public static void translationY(View view, int targetY, long duration) {
        if (view == null) {
            return;
        }
        view.animate().cancel();
        view.animate().setDuration(duration).translationY(targetY).start();
    }

    public static void removeFragmentByTag(FragmentManager fragmentManager, String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 自身下降消失底部上升显示 切换动画
     *
     * @param view    目标View
     * @param execute 消失之后执行
     */
    public static void upDownExchange(View view, Runnable execute) {
        if (view == null) {
            return;
        }
        view.animate().cancel();
        view.animate()
            .alpha(0)
            .translationY(view.getHeight() / 2f)
            .setDuration(150)
            .setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (execute != null) {
                        execute.run();
                    }
                    view.animate().cancel();
                    view.animate().alpha(1).translationY(0).setDuration(150).setListener(null).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    view.animate().setListener(null);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            })
            .start();
    }

    /**
     * 全局可视位置
     *
     * @param view
     * @return
     */
    public static Rect getGlobalVisibleRect(View view) {
        if (view == null) {
            return null;
        }
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
    }

    /**
     * 判断是否可行
     *
     * @param activity
     * @return
     */
    public static boolean isAvailable(Activity activity) {
        return activity != null && !activity.isDestroyed() && !activity.isFinishing();
    }

    public static void getViewHolderLocation(RecyclerView recyclerView, int position, int[] location) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int first = manager.findFirstVisibleItemPosition();
        View holder = recyclerView.getChildAt(position - first);
        if (holder == null) {
            return;
        }
        holder.getLocationOnScreen(location);

    }

}
