package com.froyo.ridekaro

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationService : Service() {
    public final var time:Timer? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification()
        } else {
            startForeground(2, Notification())
        }
        stopNotification()
        return START_STICKY
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotification() {
        val channelId = "harsh"
        val channelName = "ride karo"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, channelId)
        val notification: Notification = builder
            .setTimeoutAfter(30)
            .setSmallIcon(R.drawable.splash_rapido_icon)
            .setContentTitle("Payment Successfully")
            .setAutoCancel(true)
            .setContentText("Your Payment has been done")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Your Payment has been Done ,Thank You For Riding with Ride Karo ")
            )
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .build()
        startForeground(1, notification)


    }
    private fun stopNotification(){
        time = Timer()
        time!!.schedule(object : TimerTask() {
            override fun run() {
                stopSelf()
            }
        }, 40000)

    }

    override fun onDestroy() {
        super.onDestroy()
    }


}