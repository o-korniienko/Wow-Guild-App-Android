package com.wowguild.util

import android.os.Build
import android.text.TextUtils
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.res.Configuration
import android.telephony.TelephonyManager
import java.util.*

/**
 * Utils for getting info
 */
internal object Info {

    /** Returns the consumer friendly device name  */
    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return try {
            if (model.startsWith(manufacturer)) {
                capitalize(model).toString()
            } else {
                capitalize(manufacturer) + " " + model
            }
        } catch (e: Exception) {
            "unknown"
        }
    }

    /**
     * Get android version
     */
    fun getAndroidVersion(): String {
        return try {
            Build.VERSION.RELEASE
        } catch (e: java.lang.Exception) {
            "unknown"
        }
    }

    /**
     * Get device type (phone or tablet)
     */
    fun getPhoneType(context: Context): String {
        return try {
            val flagIsTab: Boolean =
                context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
            if (flagIsTab) {
                WowGuildLogger.debug(
                    context,
                    "Result: Function: get_phone_type, Class: GetInfo, flagisTab: $flagIsTab, answer: tablet"
                )
                "tablet"
            } else {
                WowGuildLogger.debug(
                    context,
                    "Result: Function: get_phone_type, Class: GetInfo, flagisTab: $flagIsTab, answer: phone"
                )
                "phone"
            }
        } catch (e: java.lang.Exception) {
            "unknown"
        }
    }

    /**
     * Get current OS Type
     * @return current OS type
     */
    fun getOSType(): String {
        return "android"
    }

    fun getDeviceType(context: Context): String {
        return getPhoneType(context)
    }

    private fun capitalize(str: String): String? {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true

        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }

}