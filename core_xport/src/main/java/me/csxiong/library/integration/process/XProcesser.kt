package me.csxiong.library.integration.process

import android.Manifest
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.lang.ref.WeakReference

/**
 * @Desc : 事件解析代理
 * @Author : Bear - 2020/4/27
 *
 * 使用fragment代理代理 activity发起跳转和接收result 控制在一个"流程"内
 */
class XProcesser {

    private final val TAG = "XProcesser"

    private var wFg: WeakReference<DelegateFragment>? = null

    private var activity: FragmentActivity? = null

    /**
     * 构造
     */
    constructor(activity: FragmentActivity) {
        this.activity = activity
        // 创建代理Fragment
        wFg = generateDelegateFragment(activity.getSupportFragmentManager());
    }

    /**
     * 创建代理Fragment
     *
     * @param fragmentManager
     * @return
     */
    private fun generateDelegateFragment(fragmentManager: FragmentManager): WeakReference<DelegateFragment> {
        var fg: Fragment? = fragmentManager.findFragmentByTag(TAG)
        val needCreate = fg == null
        if (needCreate) {
            fg = DelegateFragment()
            fragmentManager.beginTransaction().add(fg, TAG).commitNowAllowingStateLoss()
        }
        return WeakReference<DelegateFragment>(fg as DelegateFragment)
    }

    /**
     * 处理事件进程
     */
    fun execute(delegateProcess: DelegateProcess) {
        wFg?.get()?.let {
            it.execute(delegateProcess)
        }
    }

    fun executePermission(manifest: Array<Manifest.permission>) {

    }

}