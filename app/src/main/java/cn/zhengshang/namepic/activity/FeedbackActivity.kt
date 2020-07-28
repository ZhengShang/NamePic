package cn.zhengshang.namepic.activity

import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import cn.zhengshang.namepic.BaseActivity
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.tools.MailManager
import cn.zhengshang.namepic.tools.MailManager.Companion
import cn.zhengshang.namepic.tools.MailManager.SendStateListener
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
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }

        mDetailText = findViewById(R.id.detail_text)
        mContactText = findViewById(R.id.contact_text)
        mSubmit = findViewById(R.id.submit)
        mSubmit.completeText = getString(R.string.faeedback_success)
        mSubmit.errorText = getString(R.string.feedback_failed)
        mSubmit.setOnClickListener(OnClickListener {
            val progress = mSubmit.progress
            if (progress == 100 || progress == -1) {
                mSubmit.progress = 0
                return@OnClickListener
            } else if (progress in 1..99) {
                return@OnClickListener
            }
            if (mDetailText.text.isEmpty()) {
                Toast.makeText(this, R.string.empty_body, Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            mSubmit.progress = 50
            Companion.instance.sendMail("NamePic Feedback", mDetailText.text.toString() + "\rContact : " + mContactText.text)
            MailManager.instance.setSendStateListener(object : SendStateListener {
                override fun onSendState(state: Boolean) {
                    if (state) {
                        mSubmit.progress = 100
                        mDetailText.setText("")
                        mContactText.setText("")
                    } else {
                        mSubmit.progress = -1
                    }
                }
            })
        })
    }
}