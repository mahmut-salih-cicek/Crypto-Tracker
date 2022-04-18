package com.xmod.blockchainwidget2.Util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sout.cryptocurrencytracker.R
import com.sout.cryptocurrencytracker.View.MainActivity


const val channelId = "notification_channel"
const val channelName = "com.sout.cryptocurrencytracker"
class MyFirebaseMessagingService: FirebaseMessagingService() {


    /// generate the notification
    /// attach the notification created with custom layout
    /// show the notifitacion


    fun generatedNotification(title:String,meesage:String){
        val intent = Intent(this, MainActivity::class.java);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT)


        //channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelId)
            .setSmallIcon(R.drawable.main3)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)/// tıklandığında nereye gidiceğini ayarladik


        builder = builder.setContent(getRemoteView(title,meesage))

        /// create noti
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())

    }


    fun getRemoteView(title: String,meesage: String): RemoteViews {
        val remoteView = RemoteViews("com.sout.cryptocurrencytracker",R.layout.noti)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,meesage)
        remoteView.setImageViewResource(R.id.icon,R.drawable.main3)

        return remoteView

    }


    override fun onMessageReceived(RemoteMessage: RemoteMessage) {
        if (RemoteMessage.notification != null){
            generatedNotification(RemoteMessage.notification!!.title!!,RemoteMessage.notification!!.body!!)
        }
    }





}