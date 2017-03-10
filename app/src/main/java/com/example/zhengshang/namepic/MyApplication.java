package com.example.zhengshang.namepic;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhengshang on 2017/3/10.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);
    }
}
