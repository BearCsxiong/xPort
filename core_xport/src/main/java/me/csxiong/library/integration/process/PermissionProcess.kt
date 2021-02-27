package me.csxiong.library.integration.process

import android.content.Intent

class PermissionProcess : DelegateProcess() {

    override fun onExecute(delegateFragment: DelegateFragment) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        return super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}