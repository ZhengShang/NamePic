package cn.zhengshang.namepic.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.zhengshang.namepic.R;
import cn.zhengshang.namepic.tools.ViewHelper;

/**
 * Created by zhengshang on 2017/3/20.
 */

public class AboutMe extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutme);

        ViewHelper.initVIewToolbar(this, getString(R.string.about_me));
    }
}
