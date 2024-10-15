package com.wowguild.tool

import android.content.Context
import com.wowguild.util.WowGuildLogger

internal class SaveDataProvider(private val context: Context) {
    private val sharedPreferencesHandler: SharedPreferencesHandler = SharedPreferencesHandler(context.applicationContext)

    var registrationStatus: Boolean
        get() = sharedPreferencesHandler.getValueBool("device_registered", false)
        set(value) {
            sharedPreferencesHandler.save("device_registered", value)
            WowGuildLogger.debug(context, "[${javaClass.simpleName}] saving registrationstatus: $value")
        }

    var firebaseRegistrationToken: String
        get() = sharedPreferencesHandler.getValueString("firebase_registration_token")
        set(value) {
            sharedPreferencesHandler.saveString("firebase_registration_token", value)
            WowGuildLogger.debug(context, "[${javaClass.simpleName}] saving firebase_registration_token: $value")
        }

    var deviceId: String
        get() = sharedPreferencesHandler.getValueString("device_id")
        set(value) {
            sharedPreferencesHandler.saveString("device_id", value)
            WowGuildLogger.debug(context, "[${javaClass.simpleName}] saving deviceId: $value")
        }

    var logLevel: String
        get() = sharedPreferencesHandler.getValueString("logLevel")
        set(value) {
            sharedPreferencesHandler.saveString("logLevel", value)
            WowGuildLogger.debug(context, "[${javaClass.simpleName}] saving logLevel: $value")
        }

}