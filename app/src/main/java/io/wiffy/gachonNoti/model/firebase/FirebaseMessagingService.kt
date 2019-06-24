package io.wiffy.gachonNoti.model.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.wiffy.gachonNoti.R

class FirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val TAG = "FirebaseIDService"
    }


    override fun onMessageReceived(p0: RemoteMessage?) {
        if(p0?.notification !=null)
        {
            sendNotification(p0)
        }
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.e("Firebase", "FirebaseMessagingService : $p0")
    }


    private fun sendNotification(p0: RemoteMessage) {
        val title = getString(R.string.app_name)
        val message = p0.notification?.body?:""
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val channel = getString(R.string.channel)
                val channelName = getString(R.string.app_name)
                val notiChannel =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channelMessage = NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_DEFAULT)
                channelMessage.description = ""
                channelMessage.enableLights(true)
                channelMessage.enableVibration(true)
                channelMessage.setShowBadge(false)

                channelMessage.vibrationPattern = addList()
                notiChannel.createNotificationChannel(channelMessage)

                val notificationBuilder = NotificationCompat.Builder(this, channel)
                    .setSmallIcon(R.drawable.defaults)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setChannelId(channel)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(9999, notificationBuilder.build())
            }
            else -> {
                val notificationBuilder = NotificationCompat.Builder(this, "")
                    .setSmallIcon(R.drawable.defaults)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(9999, notificationBuilder.build())
            }
        }
    }

    private fun addList(): LongArray {
        val myList = LongArray(4)
        myList[0] = 100
        myList[1] = 200
        myList[2] = 100
        myList[3] = 200
        return myList
    }
}