package pl.wsei.pam.lab06.ui.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import pl.wsei.pam.lab06.Lab06Activity
import pl.wsei.pam.lab06.messageExtra
import pl.wsei.pam.lab06.notificationID
import pl.wsei.pam.lab06.titleExtra
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.LocalTime

class TaskAlarmScheduler(private val context: Context) {
    private var currentAlarmTaskId: Int? = null
    private var currentAlarmTime: Long = 0L

    fun scheduleAlarmForNextTask(tasks: List<Lab06Activity.TodoTask>) {
        val upcoming = tasks.filter { !it.isDone && !it.deadline.isBefore(LocalDate.now()) }
        val next = upcoming.minByOrNull { it.deadline }
        next?.let { task ->
            val notifyTime = LocalDateTime.of(task.deadline.minusDays(1), LocalTime.of(9, 0))
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            if (currentAlarmTaskId != task.id || notifyTime < currentAlarmTime) {
                cancelCurrentAlarm()
                scheduleAlarm(notifyTime, task.title)
                currentAlarmTaskId = task.id
                currentAlarmTime = notifyTime
            }
        }
    }

    public fun scheduleAlarm(time: Long, taskTitle: String) {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra(titleExtra, "Przypomnienie o zadaniu")
            putExtra(messageExtra, "Zadanie '$taskTitle' zbliża się do terminu!")
        }

        val basePendingIntent = PendingIntent.getBroadcast(
            context, notificationID, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, basePendingIntent)

        repeat(5) { i ->
            val repeatIntent = PendingIntent.getBroadcast(
                context, notificationID + i + 1,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time + (4 * 60 * 60 * 1000L) * (i + 1),
                repeatIntent
            )
        }
    }

    private fun cancelCurrentAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        (0..5).forEach { i ->
            val intent = PendingIntent.getBroadcast(
                context, notificationID + i,
                Intent(context, NotificationBroadcastReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
            )
            intent?.let {
                alarmManager.cancel(it)
                it.cancel()
            }
        }
    }
}
