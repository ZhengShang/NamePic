package cn.zhengshang.namepic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import cn.zhengshang.namepic.BaseFragment
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.databinding.FragFeedbackBinding
import cn.zhengshang.namepic.tools.MailManager
import cn.zhengshang.namepic.tools.navClickBack
import kotlinx.android.synthetic.main.frag_feedback.*

/**
 * Created by shangzheng on 2020/7/28.
 *            🐳🐳🐳🍒           16:38 🥥
 */
class FeedbackFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<FragFeedbackBinding>(inflater,
                R.layout.frag_feedback, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Toolbar>(R.id.toolbar).navClickBack()

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
            if (detail_text.text.isEmpty()) {
                Toast.makeText(context, R.string.empty_body, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            submit.progress = 50
            MailManager.instance.sendMail("NamePic Feedback", detail_text.text.toString() + "\rContact : " + contact_text.text)
            MailManager.instance.setSendStateListener(object : MailManager.SendStateListener {
                override fun onSendState(state: Boolean) {
                    if (state) {
                        submit.progress = 100
                        detail_text.setText("")
                        contact_text.setText("")
                    } else {
                        submit.progress = -1
                    }
                }
            })
        }
    }
}