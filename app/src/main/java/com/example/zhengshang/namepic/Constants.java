package com.example.zhengshang.namepic;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengshang on 2017/2/10.
 */

public class Constants {
    static final int TYPE_SAVE = 1;
    static final int TYPE_SHARE = 2;

    static final int TEXT_COUNT_MIN_VALUE = 1;
    static final int TEXT_COUNT_MAX_VALUE = 18;
    static final int TEXT_COUNT_DEF_VALUE = 9;

    static final int TEXT_SIZE_MIN_VALUE = 200;
    static final int TEXT_SIZE_MAX_VALUE = 400;
    static final int TEXT_SIZE_DEF_VALUE = 300;

    static final int Y_POSITION_MIN_VALUE = -500;
    static final int Y_POSITION_MAX_VALUE = 300;
    static final int Y_POSITION_DEF_VALUE = 0;

    static final String DEF_TEXT = "连";
    static final String DEF_TEXT_FONT = "simfang.ttf";

    static final int DEF_BASE_COLOR = Color.GRAY;
    static final int DEF_TEXT_COLOR = Color.WHITE;
    static final int DEF_Y_POSITION = -200;
    static final int DEF_TEXT_RADIUS = 200;

    static final int DEF_BACKGROUND_PIC = R.drawable.pic_clover;

    static List<String> getFontsList() {
        List<String> list = new ArrayList<>();
       list.add("仿宋");
       list.add("微软雅黑");
       list.add("微软雅黑粗体");
       list.add("黑体");
       list.add("楷体");
       list.add("宋体");
        return list;
    }



}
