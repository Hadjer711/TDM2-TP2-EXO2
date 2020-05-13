package com.example.mailnotifictaion

import android.annotation.TargetApi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.os.Build

import android.telephony.gsm.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startForegroundService


public class SmsReceiver:BroadcastReceiver() {


    val NOTIFICATION_ID = "notification-id"
    val NOTIFICATION = "notification"
    @RequiresApi(Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent?) {
        val extra = intent!!.extras
        if (extra != null) {
            val sms = extra.get("pdus") as Array<Any>
            for (i in sms.indices) {
                val format = extra.getString("format")
                val smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(sms[i] as ByteArray)
                } else {
                    SmsMessage.createFromPdu(sms[i] as ByteArray)
                }
                val phonenumber = smsMessage.originatingAddress
                val text = smsMessage.messageBody.toString()
                Log.d("Sonthing ", "I just think that this is the receiver ")
                Toast.makeText(context, "phone number $phonenumber \n message text : $text", Toast.LENGTH_LONG)
                    .show()
            }
            /*          /****************************************Afficher la notification *********************************/

           lateinit  var notificationManager : NotificationManager;
            lateinit  var  notificationChannel : NotificationChannel;
            lateinit var builder:Notification.Builder
              val CHANNEL_ID ="com.example.mailnotifictaion"
              val description ="the description of the notif "
            val intent = Intent(context,LauncherActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            notificationManager =  getSystemService(context,this.javaClass) as NotificationManager
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationChannel = NotificationChannel(CHANNEL_ID, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                builder = Notification.Builder(context,CHANNEL_ID)
                    .setContentTitle("Reply SMS")
                    .setContentText("mail sent to the person who just send to you a message ")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon( BitmapFactory.decodeResource( context.resources,R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent)
            }
            else{
                builder = Notification.Builder(context)
                    .setContentTitle("Reply SMS")
                    .setContentText("mail sent to the person who just send to you a message ")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon( BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234,builder.build())
        }
*/
          //////Appelr le service des notifications
            val intent = Intent(context,MyService::class.java)
            startForegroundService(context, intent)

        }
    }
}






