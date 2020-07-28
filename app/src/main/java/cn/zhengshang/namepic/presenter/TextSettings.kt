package cn.zhengshang.namepic.presenter

import cn.zhengshang.namepic.view.PicView

/**
 * Created by zhengshang on 2017/2/7.
 */
class TextSettings(private val mPicView: PicView) {

    fun setTextYPosition(position: Int) {
        mPicView.yPosition = position
        mPicView.postInvalidate()
    }

    var nameText: String?
        get() = mPicView.nameText
        set(text) {
            mPicView.nameText = text!!
            mPicView.postInvalidate()
        }

    var textCount: Int
        get() = mPicView.nameTextCount
        set(count) {
            mPicView.nameTextCount = count
            mPicView.postInvalidate()
        }

    var textSize: Int
        get() = mPicView.nameTextSize
        set(size) {
            mPicView.nameTextSize = size
            mPicView.postInvalidate()
        }

    val yPosition: Int
        get() = mPicView.yPosition

    var font: String?
        get() = mPicView.getNameFont()
        set(name) {
            mPicView.setFont(name!!)
            mPicView.postInvalidate()
        }

}