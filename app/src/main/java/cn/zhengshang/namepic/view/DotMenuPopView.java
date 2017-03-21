package cn.zhengshang.namepic.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.zhengshang.namepic.R;
import cn.zhengshang.namepic.activity.AboutMe;
import cn.zhengshang.namepic.activity.FeedbackActivity;

/**
 * Created by zhengshang on 2017/3/20.
 */

public class DotMenuPopView extends PopupWindow implements View.OnClickListener {
    private Context mContext;

    public DotMenuPopView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_dot_menu, null);
        TextView feedback = (TextView) view.findViewById(R.id.feedback);
        feedback.setOnClickListener(this);
        TextView aboutMe = (TextView) view.findViewById(R.id.about_me);
        aboutMe.setOnClickListener(this);

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        //  ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        //  this.setBackgroundDrawable(dw);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback:
                mContext.startActivity(new Intent(mContext, FeedbackActivity.class));
                break;
            case R.id.about_me:
                mContext.startActivity(new Intent(mContext, AboutMe.class));
                break;
        }
        this.dismiss();
    }

}
