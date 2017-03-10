package cn.zhengshang.namepic.presenter;

import cn.zhengshang.namepic.view.PicView;

/**
 * Created by zhengshang on 2017/2/7.
 */

public class TextSettings {
    private PicView mPicView;

    public  TextSettings(PicView picView) {
        this.mPicView = picView;
    }

    public void setNameText(String text) {
        mPicView.setNameText(text);
        mPicView.postInvalidate();
    }

    public void setTextCount(int count) {
        mPicView.setNameTextCount(count);
        mPicView.postInvalidate();
    }

    public void setTextSize(int size) {
        mPicView.setNameTextSize(size);
        mPicView.postInvalidate();
    }

    public void setTextYPosition(int position) {
        mPicView.setYPosition(position);
        mPicView.postInvalidate();
    }

    public String getNameText() {
        return mPicView.getNameText();
    }

    public int getTextCount() {
        return mPicView.getNameTextCount();
    }

    public int getTextSize() {
        return mPicView.getNameTextSize();
    }

    public int getYPosition() {
        return mPicView.getYPosition();
    }

    public String getFont() {
        return mPicView.getmNameFont();
    }

    public void setFont(String name) {
        mPicView.setFont(name);
        mPicView.postInvalidate();
    }
}
