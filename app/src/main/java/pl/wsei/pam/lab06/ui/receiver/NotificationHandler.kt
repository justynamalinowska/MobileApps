package pl.wsei.pam.lab01.lab06.notification

import android.content.Context
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import pl.wsei.pam.lab01.R

class NotificationHandler(private val context: Context) {
    private val notificationManager =
        context.getSystemService(NotificationManager::class.java)

    fun showSimpleNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Simple notification")
            .setContentText("Notification text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val NOTIFICATION_ID = 121
        const val CHANNEL_ID = "TodoApp channel"
        const val TITLE_EXTRA = "title"
        const val MESSAGE_EXTRA = "message"
    }
}