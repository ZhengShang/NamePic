package cn.zhengshang.namepic.tools;

import android.graphics.Color;

import cn.zhengshang.namepic.R;

/**
 * Created by zhengshang on 2017/2/10.
 */

public class Constants {
    public static final int TYPE_SAVE = 1;
    public static final int TYPE_SHARE = 2;

    public static final int TEXT_COUNT_MIN_VALUE = 1;
    public static final int TEXT_COUNT_MAX_VALUE = 18;
    public static final int TEXT_COUNT_DEF_VALUE = 9;

    public static final int TEXT_SIZE_MIN_VALUE = 200;
    public static final int TEXT_SIZE_MAX_VALUE = 400;
    public static final int TEXT_SIZE_DEF_VALUE = 300;

    public static final int Y_POSITION_MIN_VALUE = -500;
    public static final int Y_POSITION_MAX_VALUE = 300;

    public static final String DEF_TEXT = "连";
    public static final String DEF_TEXT_FONT = "simfang.ttf";

    public static final int DEF_BASE_COLOR = Color.GRAY;
    public static final int DEF_TEXT_COLOR = Color.WHITE;
    public static final int DEF_Y_POSITION = -200;
    public static final int DEF_TEXT_RADIUS = 200;

    public static final int DEF_BACKGROUND_PIC = R.drawable.pic_clover;
    public static final boolean DEF_PIC_SHOW = true;


    /**
     * 如下为需要保存的设置信息
     */
    public static final String SHARED_PREFS_NAME = "namePic";
    public static final String COLOR_SET_TV11 = "color_set_tv11";      //颜色选项里面的第11个选项的颜色值
    public static final String COLOR_SET_GROUP_INDEX = "color_set_group_index";      //颜色组的radioGroup选中的index
    public static final String COLOR_SET_BASE_COLOR = "color_set_base_color";      //底色
    public static final String COLOR_SET_TEXT_COLOR = "color_set_text_color";      //文字颜色
    public static final String TEXT_SET_CENTER_TEXT = "text_set_center_text";    //中间显示的文字
    public static final String TEXT_SET_GROUP_INDEX = "text_set_group_index";   //文字设置组的radioGroup选中的index
    public static final String TEXT_SET_COUNT = "text_set_count";      //文字数量
    public static final String TEXT_SET_SIZE = "text_set_size";      //文字大小
    public static final String TEXT_SET_Y_POSITION = "text_set_y_position";      //文字Y轴偏移量
    public static final String TEXT_SET_FONT = "text_set_font";     //文字设置的字体滚轮中的index
    public static final String PIC_SET_SHOW = "pic_set_show";   //设置背景图片是否显示
}