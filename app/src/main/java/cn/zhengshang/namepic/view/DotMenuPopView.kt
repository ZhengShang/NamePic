package cn.zhengshang.namepic.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import cn.zhengshang.namepic.R

/**
 * Created by zhengshang on 2017/3/20.
 */
class DotMenuPopView(private val mContext: Context) : PopupWindow(), OnClickListener {
    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.feedback -> mContext.startActivity(Intent(mContext, FeedbackActivity::class.java))
//            R.id.about_me -> mContext.startActivity(Intent(mContext, AboutMe::class.java))
//        }
        dismiss()
    }

    init {
        val view = LayoutInflater.from(mContext).inflate(R.layout.view_dot_menu, null)
        val feedback = view.findViewById<View>(R.id.feedback) as TextView
        feedback.setOnClickListener(this)
        val aboutMe = view.findViewById<View>(R.id.about_me) as TextView
        aboutMe.setOnClickListener(this)
        /* 设置弹出窗口特征 */ // 设置视图
        this.contentView = view
        // 设置弹出窗体的宽和高
        this.height = LinearLayout.LayoutParams.WRAP_CONTENT
        this.width = LinearLayout.LayoutParams.WRAP_CONTENT
        // 设置弹出窗体可点击
        this.isFocusable = true
        // 实例化一个ColorDrawable颜色为半透明
//  ColorDrawable dw = new ColorDrawable(0xb0000000);
// 设置弹出窗体的背景
//  this.setBackgroundDrawable(dw);
    }
}