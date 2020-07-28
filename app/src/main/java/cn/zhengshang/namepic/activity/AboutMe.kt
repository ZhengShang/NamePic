package cn.zhengshang.namepic.activity

import android.os.Bundle
import android.widget.Toolbar
import cn.zhengshang.namepic.BaseActivity
import cn.zhengshang.namepic.R

/**
 * Created by zhengshang on 2017/3/20.
 */
class AboutMe : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aboutme)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
    }
}