package cn.zhengshang.namepic.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.Toast
import cn.zhengshang.namepic.BaseActivity
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.tools.MailManager
import cn.zhengshang.namepic.tools.MailManager.Companion
import cn.zhengshang.namepic.tools.MailManager.SendStateListener
import cn.zhengshang.namepic.tools.ViewHelper
import com.dd.processbutton.iml.ActionProcessButton

/**
 * Created by zhengshang on 2017/3/20.
 */
class FeedbackActivity : BaseActivity() {
    private lateinit var mDetailText: EditText
    private lateinit var mContactText: EditText
    private lateinit var mSubmit: ActionProcessButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback)
        ViewHelper.initVIewToolbar(this, getString(R.string.feedback))
        mDetailText = findViewById(R.id.detail_text)
        mContactText = findViewById(R.id.contact_text)
        mSubmit = findViewById(R.id.submit)
        mSubmit.setCompleteText(getString(R.string.faeedback_success))
        mSubmit.setErrorText(getString(R.string.feedback_failed))
        mSubmit.setOnClickListener(OnClickListener {
            val progress = mSubmit.getProgress()
            if (progress == 100 || progress == -1) {
                mSubmit.setProgress(0)
                return@OnClickListener
            } else if (progress > 0 && progress < 100) {
                return@OnClickListener
            }
            if (TextUtils.isEmpty(mDetailText.getText())) {
                Toast.makeText(this@FeedbackActivity, R.string.empty_body, Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            mSubmit.setProgress(50)
            Companion.instance.sendMail("NamePic Feedback", mDetailText.getText().toString() + "\rContact : " + mContactText.getText())
            MailManager.instance.setSendStateListener(object : SendStateListener {
                override fun onSendState(state: Boolean) {
                    if (state) {
                        mSubmit.setProgress(100)
                        mDetailText.setText("")
                        mContactText.setText("")
                    } else {
                        mSubmit.setProgress(-1)
                    }
                }
            })
        })
    }
}