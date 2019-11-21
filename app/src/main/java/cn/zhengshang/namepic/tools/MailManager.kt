package cn.zhengshang.namepic.tools

import android.os.AsyncTask
import java.io.IOException
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * 邮件管理类
 * Created by DevWiki on 2016/8/21.
 */
class MailManager private constructor() {
    private var sendStateListener: SendStateListener? = null

    private object InstanceHolder {
        internal val instance = MailManager()
    }

    internal inner class MailTask(private val mimeMessage: MimeMessage) : AsyncTask<Void?, Void?, Boolean>() {

        override fun onPostExecute(aBoolean: Boolean) {
            sendStateListener!!.onSendState(aBoolean)
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            return try {
                Transport.send(mimeMessage)
                java.lang.Boolean.TRUE
            } catch (e: MessagingException) {
                e.printStackTrace()
                java.lang.Boolean.FALSE
            }
        }

    }

    fun sendMail(title: String, content: String) {
        val mimeMessage = createMessage(title, content)
        val mailTask = MailTask(mimeMessage)
        mailTask.execute()
    }

    fun sendMailWithFile(title: String, content: String, filePath: String) {
        val mimeMessage = createMessage(title, content)
        appendFile(mimeMessage, filePath)
        val mailTask = MailTask(mimeMessage)
        mailTask.execute()
    }

    fun sendMailWithMultiFile(title: String, content: String, pathList: List<String>) {
        val mimeMessage = createMessage(title, content)
        appendMultiFile(mimeMessage, pathList)
        val mailTask = MailTask(mimeMessage)
        mailTask.execute()
    }

    private val authenticator: Authenticator
        private get() = object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(SENDER_NAME, SENDER_PASS)
            }
        }

    private fun createMessage(title: String, content: String): MimeMessage {
        val properties = System.getProperties()
        properties[KEY_MAIL_HOST] = VALUE_MAIL_HOST
        properties[KEY_MAIL_AUTH] = VALUE_MAIL_AUTH
        properties[KEY_MAIL_PORT] = VALUE_MAIL_PORT //之前漏掉了这个port，害得我浪费了个把小时，好气。
        val session = Session.getInstance(properties, authenticator)
        val mimeMessage = MimeMessage(session)
        try {
            mimeMessage.setFrom(InternetAddress(SENDER_NAME))
            val addresses = arrayOf(InternetAddress(SENDER_NAME))
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, addresses)
            mimeMessage.subject = title
            val textPart = MimeBodyPart()
            textPart.setContent(content, "text/html")
            textPart.setText(content, "UTF-8")
            val multipart: Multipart = MimeMultipart()
            multipart.addBodyPart(textPart)
            mimeMessage.setContent(multipart)
            mimeMessage.sentDate = Date()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
        return mimeMessage
    }

    private fun appendFile(message: MimeMessage, filePath: String) {
        try {
            val multipart = message.content as Multipart
            val filePart = MimeBodyPart()
            filePart.attachFile(filePath)
            multipart.addBodyPart(filePart)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun appendMultiFile(message: MimeMessage, pathList: List<String>) {
        try {
            val multipart = message.content as Multipart
            for (path in pathList) {
                val filePart = MimeBodyPart()
                filePart.attachFile(path)
                multipart.addBodyPart(filePart)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    interface SendStateListener {
        fun onSendState(state: Boolean)
    }

    fun setSendStateListener(sendStateListener: SendStateListener?) {
        this.sendStateListener = sendStateListener
    }

    companion object {
        private const val SENDER_NAME = "1287164352@qq.com"
        private const val SENDER_PASS = "ucjckjkdjeocgifb"
        private const val KEY_MAIL_HOST = "mail.smtp.host"
        private const val KEY_MAIL_AUTH = "mail.smtp.auth"
        private const val VALUE_MAIL_HOST = "smtp.qq.com"
        private const val VALUE_MAIL_AUTH = "true"
        private const val KEY_MAIL_PORT = "mail.smtp.port"
        private const val VALUE_MAIL_PORT = "587"
        val instance: MailManager
            get() = InstanceHolder.instance
    }
}