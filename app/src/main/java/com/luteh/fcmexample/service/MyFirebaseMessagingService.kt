package com.luteh.fcmexample.service

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.luteh.fcmexample.config.Config
import android.R.attr.bitmap
import android.app.NotificationManager
import android.app.NotificationChannel
import android.annotation.SuppressLint
import android.app.Notification
import android.os.Build
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.luteh.fcmexample.MainActivity
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.luteh.fcmexample.GlideApp
import com.luteh.fcmexample.R
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingServ"

    private fun sendNotification(bitmap: Drawable) {
        Log.d(TAG, "sendNotification: ")
        val style = NotificationCompat.BigPictureStyle()
        style.bigPicture(bitmap.toBitmap())

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "101"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX)

            //Configure Notification Channel
            notificationChannel.description = "Game Notifications"
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(Config.title)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentText(Config.content)
            .setContentIntent(pendingIntent)
            .setStyle(style)
            .setLargeIcon(bitmap.toBitmap())
            .setWhen(System.currentTimeMillis())
            .setPriority(Notification.PRIORITY_MAX)


        notificationManager.notify(1, notificationBuilder.build())
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        if (!remoteMessage?.data.isNullOrEmpty())
            getImage(remoteMessage!!)
    }

    @Suppress("DEPRECATION")
    private fun getImage(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        Config.title = data["title"]!!
        Config.content = data["content"]!!
        Config.imageUrl = data["imageUrl"]!!
        Config.gameUrl = data["gameUrl"]!!

        Log.d(TAG, "getImage: ${remoteMessage.messageId}")

        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.post {
            // Get image from data Notification
            GlideApp.with(this)
                .load(Config.imageUrl)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        sendNotification(resource)
                    }

                })
        }
    }
}
