package cn.zhengshang.namepic.activity

import androidx.appcompat.app.AppCompatActivity
import com.umeng.analytics.MobclickAgent

/**
 * Created by zhengshang on 2017/3/10.
 */
open class BaseActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }
}