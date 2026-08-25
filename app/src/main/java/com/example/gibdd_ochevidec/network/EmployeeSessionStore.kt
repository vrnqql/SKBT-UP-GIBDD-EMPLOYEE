package com.example.gibdd_ochevidec.network

import android.content.Context

data class EmployeeSession(val deviceId: String, val role: String?, val accessToken: String) {
    val authorization: String get() = "Bearer $accessToken"
}

class EmployeeSessionStore(context: Context) {
    private val preferences = context.getSharedPreferences("employee_session", Context.MODE_PRIVATE)

    fun save(response: RegisterDeviceResponse) {
        preferences.edit().putString("device_id", response.deviceId)
            .putString("role", response.role).putString("access_token", response.accessToken).apply()
    }

    fun load(): EmployeeSession? {
        val deviceId = preferences.getString("device_id", null) ?: return null
        val accessToken = preferences.getString("access_token", null) ?: return null
        return EmployeeSession(deviceId, preferences.getString("role", null), accessToken)
    }
}
