package me.csxiong.library.integration.process

import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * @Desc : 代理Fragment
 * @Author : Bear - 2020/4/27
 */
class DelegateFragment : Fragment() {

    /**
     * 处理类集合
     *
     * 基本只会存在一个
     *
     * 如果processes在dialog中
     *
     */
    private var processes: ArrayList<DelegateProcess> = ArrayList()

    fun execute(delegateProcess: DelegateProcess) {
        processes.add(delegateProcess)
        delegateProcess.onExecute(this)
    }

    /**
     * 处理结果
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //处理delegate事件
        val mIterator = processes.iterator()
        while (mIterator.hasNext()) {
            val next = mIterator.next()
            if (next.onActivityResult(requestCode, resultCode, data)) {
                mIterator.remove()
            }
        }
    }

    /**
     * 请求权限处理结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //处理delegate事件
        val mIterator = processes.iterator()
        while (mIterator.hasNext()) {
            val next = mIterator.next()
            if (next.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                mIterator.remove()
            }
        }
    }
}