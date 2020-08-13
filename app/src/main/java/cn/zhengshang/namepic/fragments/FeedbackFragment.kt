package cn.zhengshang.namepic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.tools.MailManager
import cn.zhengshang.namepic.tools.navClickBack
import com.dd.processbutton.iml.ActionProcessButton

/**
 * Created by shangzheng on 2020/7/28.
 *            🐳🐳🐳🍒           16:38 🥥
 */
class FeedbackFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.frag_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Toolbar>(R.id.toolbar).navClickBack()

        val detailText = view.findViewById<EditText>(R.id.detail_text)
        val contactText = view.findViewById<EditText>(R.id.contact_text)
        val submit = view.findViewById<ActionProcessButton>(R.id.submit)
        submit.completeText = getString(R.string.faeedback_success)
        submit.errorText = getString(R.string.feedback_failed)
        submit.setOnClickListener {
            val progress = submit.progress
            if (progress == 100 || progress == -1) {
                submit.progress = 0
                return@setOnClickListener
            } else if (progress in 1..99) {
                return@setOnClickListener
            }
            if (detailText.text.isEmpty()) {
                Toast.makeText(context, R.string.empty_body, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            submit.progress = 50
            MailManager.instance.sendMail("NamePic Feedback", detailText.text.toString() + "\rContact : " + contactText.text)
            MailManager.instance.setSendStateListener(object : MailManager.SendStateListener {
                override fun onSendState(state: Boolean) {
                    if (state) {
                        submit.progress = 100
                        detailText.setText("")
                        contactText.setText("")
                    } else {
                        submit.progress = -1
                    }
                }
            })
        }
    }
}