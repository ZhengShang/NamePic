package cn.zhengshang.namepic.presenter

import cn.zhengshang.namepic.view.PicView

/**
 * Created by zhengshang on 2017/2/6.
 */
class ColorSettings(private val mPicView: PicView) {

    fun setCenterTextColor(color: Int) {
        mPicView.centerTextColor = color
        mPicView.postInvalidate()
    }

    var basePicColor: Int
        get() = mPicView.baseColor
        set(color) {
            mPicView.baseColor = color
            mPicView.postInvalidate()
        }

    val textColor: Int
        get() = mPicView.centerTextColor

}