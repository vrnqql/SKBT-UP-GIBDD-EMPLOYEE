package com.example.gibdd_ochevidec.network

import android.content.Context
import java.security.MessageDigest
import java.util.UUID


object DeviceFingerprint {

    fun getHash(context: Context): String {

        val preferences = context.getSharedPreferences(
            "device_fingerprint",
            Context.MODE_PRIVATE
        )

        var installationId = preferences.getString(
            "installation_id",
            null
        )

        if (installationId == null) {

            installationId = UUID.randomUUID().toString()

            preferences
                .edit()
                .putString(
                    "installation_id",
                    installationId
                )
                .apply()
        }

        return sha256(installationId)
    }


    private fun sha256(value: String): String {

        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(value.toByteArray())

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }
}