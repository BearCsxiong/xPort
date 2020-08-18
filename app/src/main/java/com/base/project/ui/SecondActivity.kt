package com.base.project.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.base.project.R
import com.base.project.databinding.ActivitySecondBinding
import me.csxiong.library.base.BaseActivity

@Route(path = "/main/second")
class SecondActivity : BaseActivity<ActivitySecondBinding>() {

    override fun initView() {
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_second
    }
}