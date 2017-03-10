package cn.zhengshang.namepic.presenter;

import cn.zhengshang.namepic.view.PicView;

/**
 * Created by zhengshang on 2017/2/6.
 */

public class ColorSettings {
    private PicView mPicView;

    public  ColorSettings(PicView picView) {
        this.mPicView = picView;
    }

    public void setBasePicColor(int color) {
        mPicView.setBaseColor(color);
        mPicView.postInvalidate();
    }

    public void setCenterTextColor(int color) {
        mPicView.setCenterTextColor(color);
        mPicView.postInvalidate();
    }

    public int getBasePicColor() {
        return mPicView.getBaseColor();
    }

    public int getTextColor() {
        return mPicView.getCenterTextColor();
    }
}
