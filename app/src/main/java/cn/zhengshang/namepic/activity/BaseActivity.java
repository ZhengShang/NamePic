package cn.zhengshang.namepic.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhengshang on 2017/3/10.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
