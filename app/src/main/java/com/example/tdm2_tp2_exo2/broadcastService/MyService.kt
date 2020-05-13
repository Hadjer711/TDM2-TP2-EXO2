package com.example.mailnotifictaion



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder

import android.widget.Button
import androidx.core.app.NotificationCompat
import com.example.tdm2_tp2_exo2.R
import java.time.LocalDateTime
import java.util.*



class MyService : Service() {

    private var alarmeHour: Int? = null
    private var alarmeMinute: Int? = null
    private val time = Timer()
    private var mediaPlayer = MediaPlayer()



    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int):Int {
        alarmeHour = intent?.getIntExtra("adhanHour",0)
        alarmeMinute = intent?.getIntExtra("adhanMinute", 0)

       /* val notificationIntent = Intent(this, MainActivity::class.java)
        time.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {


            }

        }, 0, 2000)

                           //Redemarrer après terminaison. Indépendant des intents*/

        try {
            mediaPlayer.start()

            val vibre = longArrayOf(500, 1000)
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("Automatic Reply") // title for notification
                .setContentText("an email was just sent to the person who sent you a message")// message for notification
                .setSmallIcon(R.drawable.ic_launcher) //small icon for notification
                .build()
            startForeground(1, notification)  // Services d’Arrière-Plan
            //Notification channel
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Adhan Service channel",
                    NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "YOUR_NOTIFICATION_CHANNEL_DISCRIPTION"
                manager.createNotificationChannel(channel)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer!!.stop()
        time.cancel()
        super.onDestroy()
    }

    companion object {
        const val CHANNEL_ID = "MyNotificationSmsServiceChannel"
    }
}