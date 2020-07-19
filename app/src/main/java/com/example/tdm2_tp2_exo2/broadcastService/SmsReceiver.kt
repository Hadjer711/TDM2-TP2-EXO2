package com.example.tdm2_tp2_exo2.broadcastService;

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.provider.Telephony
import android.util.Log
import android.widget.RemoteViews

import com.example.tdm2_tp2_exo2.MainActivity
import com.example.tdm2_tp2_exo2.R
import com.example.tdm2_tp2_exo2.email.JavaMailAPI


private const val TAG = "SMSReceiver"

open class SmsReceiver : BroadcastReceiver() {
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId="com.example.tdm2_tp2_exo2"
    private val description= "SMS Notification"


    override fun onReceive(context: Context, intent: Intent) {


        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Log.d("BroadcastReceiver", "SMS received")
            // Will do stuff with message here
            senEmail()
            notification(context)







        }
    }

    private fun senEmail() {
        val mEmail: String = "gh_ishakboushaki@esi.dz"
        val mSubject: String = "from contact app"
        val mMessage: String = "the mail sending works!"
        val javaMailAPI = JavaMailAPI(
            MainActivity().context(),
            mEmail,
            mSubject,
            mMessage
        )
        javaMailAPI.execute()
    }

    private fun notification(context: Context){
        val intent = Intent(context,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(context.packageName,R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title,"Notification de EXO 2")
        contentView.setTextViewText(R.id.tv_content,"Vous avez recu un SMS, un Email a été envoyé")

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context,channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)
        }else{

            builder = Notification.Builder(context)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)
        }

        notificationManager.notify(1234,builder.build())

    }


}