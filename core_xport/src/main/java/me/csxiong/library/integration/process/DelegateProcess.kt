package me.csxiong.library.integration.process

import android.content.Intent

/**
 * @Desc : 代理解析类
 * @Author : Bear - 2020/4/27
 */
open abstract class DelegateProcess {

    /**
     * 执行
     * 使用delegateFragment中的startActivityForResult
     * 能够获取到对应的响应
     */
    abstract fun onExecute(delegateFragment: DelegateFragment)

    /**
     * 结果回调
     *
     * @return 是否处理成功 处理成功 直接删除
     */
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean

    /**
     * 权限结果
     */
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        return true
    }

}