package com.example.zhengshang.namepic;

/**
 * Created by zhengshang on 2017/2/7.
 */

public class TextSettings {
    private PicView mPicView;

    TextSettings(PicView picView) {
        this.mPicView = picView;
    }

    void setNameText(String text) {
        mPicView.setNameText(text);
        mPicView.postInvalidate();
    }

    void setTextCount(int count) {
        mPicView.setNameTextCount(count);
        mPicView.postInvalidate();
    }

    void setTextSize(int size) {
        mPicView.setNameTextSize(size);
        mPicView.postInvalidate();
    }

    void setTextYPosition(int position) {
        mPicView.setYPosition(position);
        mPicView.postInvalidate();
    }

    String getNameText() {
        return mPicView.getNameText();
    }

    int getTextCount() {
        return mPicView.getNameTextCount();
    }
    int getTextSize() {
        return mPicView.getNameTextSize();
    }

    int getYPosition() {
        return mPicView.getYPosition();
    }

    String getFont() {
        return mPicView.getmNameFont();
    }

    void setFont(String name) {
        mPicView.setFont(name);
        mPicView.postInvalidate();
    }
}
