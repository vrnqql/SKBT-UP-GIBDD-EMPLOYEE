package com.example.gibdd_ochevidec.network

import com.google.gson.annotations.SerializedName


data class HealthResponse(
    val status: String
)


data class RegisterDeviceRequest(
    @SerializedName("fingerprint_hash")
    val fingerprintHash: String,

    @SerializedName("push_token")
    val pushToken: String? = null
)


data class RegisterDeviceResponse(
    @SerializedName("device_id")
    val deviceId: String,

    val role: String?,

    @SerializedName("access_token")
    val accessToken: String
)