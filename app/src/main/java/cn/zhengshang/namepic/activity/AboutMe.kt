package cn.zhengshang.namepic.activity

import android.os.Bundle
import cn.zhengshang.namepic.BaseActivity
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.tools.ViewHelper

/**
 * Created by zhengshang on 2017/3/20.
 */
class AboutMe : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aboutme)
        ViewHelper.initVIewToolbar(this, getString(R.string.about_me))
    }
}