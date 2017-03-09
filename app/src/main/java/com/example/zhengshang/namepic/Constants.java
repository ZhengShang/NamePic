package com.example.zhengshang.namepic;

import android.graphics.Color;

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

    static final String DEF_TEXT = "连";
    static final String DEF_TEXT_FONT = "simfang.ttf";

    static final int DEF_BASE_COLOR = Color.GRAY;
    static final int DEF_TEXT_COLOR = Color.WHITE;
    static final int DEF_Y_POSITION = -200;
    static final int DEF_TEXT_RADIUS = 200;

    static final int DEF_BACKGROUND_PIC = R.drawable.pic_clover;
    static final boolean DEF_PIC_SHOW = true;


    /**
     * 如下为需要保存的设置信息
     */
    static final String SHARED_PREFS_NAME = "namePic";
    static final String COLOR_SET_GROUP_INDEX = "color_set_group_index";      //颜色组的radioGroup选中的index
    static final String COLOR_SET_BASE_COLOR = "color_set_base_color";      //底色
    static final String COLOR_SET_TEXT_COLOR = "color_set_text_color";      //文字颜色
    static final String TEXT_SET_CENTER_TEXT = "text_set_center_text";    //中间显示的文字
    static final String TEXT_SET_GROUP_INDEX = "text_set_group_index";   //文字设置组的radioGroup选中的index
    static final String TEXT_SET_COUNT = "text_set_count";      //文字数量
    static final String TEXT_SET_SIZE = "text_set_size";      //文字大小
    static final String TEXT_SET_Y_POSITION = "text_set_y_position";      //文字Y轴偏移量
    static final String TEXT_SET_FONT = "text_set_font";     //文字设置的字体滚轮中的index
    static final String PIC_SET_SHOW = "pic_set_show";   //设置背景图片是否显示
}
