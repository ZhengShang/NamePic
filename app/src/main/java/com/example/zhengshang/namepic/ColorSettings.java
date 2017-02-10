package com.example.zhengshang.namepic;

/**
 * Created by zhengshang on 2017/2/6.
 */

public class ColorSettings {
    private PicView mPicView;

    ColorSettings(PicView picView) {
        this.mPicView = picView;
    }

    void setBasePicColor(int color) {
        mPicView.setBaseColor(color);
        mPicView.postInvalidate();
    }

    void setCenterTextColor(int color) {
        mPicView.setCenterTextColor(color);
        mPicView.postInvalidate();
    }

    int getBasePicColor() {
        return mPicView.getBaseColor();
    }
}
