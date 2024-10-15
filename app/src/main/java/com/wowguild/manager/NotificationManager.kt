package com.wowguild.manager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.wowguild.R
import com.wowguild.util.WowGuildLogger
import kotlin.random.Random

class NotificationManager(
    private val context: Context,
    private val notificationIconResourceId: Int = R.mipmap.ic_launcher
) {

    companion object {
        const val MAX_NOTIFICATIONS = 25

        const val DEFAULT_NOTIFICATION_CHANNEL_ID = "wowguild.app.notification.channel"

        const val DEFAULT_NOTIFICATION_GROUP_ID = "wowguild.app.notification.group"

        const val NOTIFICATION_CHANNEL_NAME = "WowGuild channel"

        const val NOTIFICATION_TAG = "wow_guild_n_t"

        const val DEFAULT_SUMMARY_NOTIFICATION_ID = 0

        const val SUMMARY_NOTIFICATION_TAG = "wow_guild_s_n_t"
    }

    fun constructNotification(
        notification: RemoteMessage.Notification
    ): NotificationCompat.Builder? {

        try {

            val builder =
                NotificationCompat.Builder(
                    context.applicationContext,
                    DEFAULT_NOTIFICATION_CHANNEL_ID
                ).apply {
                    setGroup(DEFAULT_NOTIFICATION_GROUP_ID)
                    priority = NotificationCompat.PRIORITY_MAX

                    setAutoCancel(true)
                    setContentTitle(notification.title)
                    setContentText(notification.body)
                    setSmallIcon(notificationIconResourceId)
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        setStyle(NotificationCompat.BigTextStyle())
                    } else {
                        setGroupSummary(true)
                    }
                }

            return builder
        } catch (e: Exception) {
            e.printStackTrace()
            WowGuildLogger.debug(context, "notification: $notification")
            return null
        }
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(
        notificationConstruct: NotificationCompat.Builder,
        notificationId: Int
    ): Boolean {
        try {
            val notification = notificationConstruct.build()
            NotificationManagerCompat.from(context.applicationContext).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(
                        DEFAULT_NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        notificationChannel.setAllowBubbles(true)
                    }

                    createNotificationChannel(
                        notificationChannel
                    )
                }

                notify(
                    getNotificationTag(),
                    notificationId,
                    notification
                )

            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun flagUpdateCurrent(mutable: Boolean): Int {
        return if (mutable) {
            if (Build.VERSION.SDK_INT >= 31) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        }
    }

    private fun getNotificationTag(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            NOTIFICATION_TAG
        } else {
            SUMMARY_NOTIFICATION_TAG
        }
    }

    fun getNotificationId(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Random.nextInt(
                DEFAULT_SUMMARY_NOTIFICATION_ID + 1,
                Int.MAX_VALUE - 10
            )
        } else {
            DEFAULT_SUMMARY_NOTIFICATION_ID
        }
    }
}