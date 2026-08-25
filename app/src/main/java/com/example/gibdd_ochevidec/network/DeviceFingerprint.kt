package com.example.gibdd_ochevidec.network

import android.content.Context
import java.security.MessageDigest
import java.util.UUID

object DeviceFingerprint {

    private const val PREFS_NAME = "device_prefs"
    private const val INSTALL_ID_KEY = "install_id"

    fun getFingerprintHash(context: Context): String {

        val preferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        var installId = preferences.getString(
            INSTALL_ID_KEY,
            null
        )

        if (installId == null) {
            installId = UUID.randomUUID().toString()

            preferences
                .edit()
                .putString(INSTALL_ID_KEY, installId)
                .apply()
        }

        return sha256(installId)
    }

    private fun sha256(value: String): String {

        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(value.toByteArray())

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }
}