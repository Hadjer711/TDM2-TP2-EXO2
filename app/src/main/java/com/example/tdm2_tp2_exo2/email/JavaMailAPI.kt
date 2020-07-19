package com.example.tdm2_tp2_exo2.email

import android.os.AsyncTask
import com.example.tdm2_tp2_exo2.MainActivity
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class JavaMailAPI(
    private val context: MainActivity,
    private val email: String,
    private val subject: String,
    private val message: String
) :
    AsyncTask<Void?, Void?, Void?>() {
    private var session: Session? = null

    override fun doInBackground(vararg params: Void?): Void? {
        val properties = Properties()
        properties["mail.smtp.ssl.trust"]= "smtp.gmail.com"
        properties["mail.smtp.ssl.enable"]= "true"
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.socketFactory.port"] = "465"
        properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.port"] = "465"
        session =
            Session.getDefaultInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(
                        Utils.EMAIL,
                        Utils.PASSWORD
                    )
                }
            })
        val mimeMessage = MimeMessage(session)
        try {
            mimeMessage.setFrom(InternetAddress(Utils.EMAIL))
            mimeMessage.addRecipients(
                Message.RecipientType.TO,
                InternetAddress(email).toString()
            )
            mimeMessage.subject = subject
            mimeMessage.setText(message)
            Transport.send(mimeMessage)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
        return null
    }


}