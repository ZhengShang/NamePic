package cn.zhengshang.namepic.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import cn.zhengshang.namepic.R

/**
 * Created by zhengshang on 2017/2/21.
 */
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, 500)
    }
}