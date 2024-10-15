package com.wowguild


import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wowguild.manager.NotificationManager
import com.wowguild.tool.SaveDataProvider
import com.wowguild.util.Info
import com.wowguild.util.WowGuildLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class PushFirebaseService : FirebaseMessagingService() {


    private lateinit var saveDataProvider: SaveDataProvider
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        saveDataProvider = SaveDataProvider(applicationContext)
        notificationManager = NotificationManager(this);

        WowGuildLogger.debug(
            applicationContext,
            "${javaClass.simpleName}.onCreate: service created"
        )

        //onNewToken(pushSdkSavedDataProvider.firebase_registration_token) //debug only
    }

    override fun onDestroy() {
        super.onDestroy()
        WowGuildLogger.debug(
            applicationContext,
            "${javaClass.simpleName}.onDestroy: service destroyed"
        )
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        WowGuildLogger.debug(
            applicationContext,
            "${javaClass.simpleName}.onNewToken: New token received: $newToken"
        )
        if (newToken != "") {
            try {
                val saveDataProvider = SaveDataProvider(applicationContext)
                saveDataProvider.firebaseRegistrationToken = newToken
                WowGuildLogger.debug(
                    applicationContext,
                    "${javaClass.simpleName}.onNewToken: local update: success"
                )
            } catch (e: Exception) {
                WowGuildLogger.error(
                    "${javaClass.simpleName}.onNewToken: local update error: ${
                        Log.getStackTraceString(
                            e
                        )
                    }}"
                )
            }

            CoroutineScope(Dispatchers.IO).launch {
                updateDeviceRegistration(newToken)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        WowGuildLogger.debug(
            applicationContext,
            "${javaClass.simpleName}.onMessageReceived: started"
        )
        super.onMessageReceived(remoteMessage)

        WowGuildLogger.debugFirebaseRemoteMessage(applicationContext, remoteMessage)

        if (isAppInForeground()) {
            onReceiveNotification(remoteMessage.notification)
        }

        println(remoteMessage.notification?.title)
        println(remoteMessage.notification?.body)
    }

    private fun onReceiveNotification(notification: RemoteMessage.Notification?) {
        notification?.let {
            val constructNotification = notificationManager.constructNotification(it)
            if (constructNotification != null) {
                val notificationId = notificationManager.getNotificationId()
                notificationManager.sendNotification(constructNotification, notificationId)
            }
        }
    }

    private fun isAppInForeground(): Boolean {
        val isInForeground =
            ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        WowGuildLogger.debug(applicationContext, "App is in foreground: $isInForeground")
        return isInForeground
    }

    private suspend fun updateDeviceRegistration(newToken: String) {
        coroutineScope {
            try {
                if (saveDataProvider.registrationStatus
                    && saveDataProvider.firebaseRegistrationToken != ""
                ) {
                    val localPhoneInfoNewToken = Info.getDeviceType(applicationContext)
                    WowGuildLogger.debug(
                        applicationContext,
                        "${javaClass.simpleName}.onNewToken: localPhoneInfoNewToken: $localPhoneInfoNewToken"
                    )

                } else {
                    WowGuildLogger.debug(
                        applicationContext,
                        "${javaClass.simpleName}.onNewToken: update: failed"
                    )
                }
            } catch (e: Exception) {
                WowGuildLogger.debug(
                    applicationContext,
                    "${javaClass.simpleName}.onNewToken: update error: ${Log.getStackTraceString(e)}"
                )
            }
        }
    }
}