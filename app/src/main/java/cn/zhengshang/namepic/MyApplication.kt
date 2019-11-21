package cn.zhengshang.namepic

import android.app.Application

/**
 * Created by zhengshang on 2017/3/10.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        MobclickAgent.setCatchUncaughtExceptions(true);
//        MobclickAgent.setDebugMode(true);
    }
}