package cn.zhengshang.namepic.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dd.processbutton.iml.ActionProcessButton;

import cn.zhengshang.namepic.R;
import cn.zhengshang.namepic.tools.MailManager;
import cn.zhengshang.namepic.tools.ViewHelper;

/**
 * Created by zhengshang on 2017/3/20.
 */

public class FeedbackActivity extends BaseActivity {
    private EditText mDetailText, mContactText;
    private ActionProcessButton mSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        ViewHelper.initVIewToolbar(this, getString(R.string.feedback));
        mDetailText = findViewById(R.id.detail_text);
        mContactText = findViewById(R.id.contact_text);
        mSubmit =  findViewById(R.id.submit);
        mSubmit.setCompleteText(getString(R.string.faeedback_success));
        mSubmit.setErrorText(getString(R.string.feedback_failed));
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = mSubmit.getProgress();
                if (progress == 100 || progress == -1) {
                    mSubmit.setProgress(0);
                    return;
                } else if (progress > 0 && progress < 100) {
                    return;
                }

                if (TextUtils.isEmpty(mDetailText.getText())) {
                    Toast.makeText(FeedbackActivity.this, R.string.empty_body, Toast.LENGTH_SHORT).show();
                    return;
                }

                mSubmit.setProgress(50);
                MailManager.getInstance().sendMail("NamePic Feedback", mDetailText.getText() + "\rContact : " + mContactText.getText());
                MailManager.getInstance().setSendStateListener(new MailManager.SendStateListener() {
                    @Override
                    public void onSendState(boolean state) {
                        if (state) {
                            mSubmit.setProgress(100);
                            mDetailText.setText("");
                            mContactText.setText("");
                        } else {
                            mSubmit.setProgress(-1);
                        }
                    }
                });

            }
        });
    }
}
