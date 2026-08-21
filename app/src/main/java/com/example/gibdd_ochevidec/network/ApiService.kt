package com.example.gibdd_ochevidec.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {

    @GET("health")
    suspend fun health(): HealthResponse

    @POST("api/v1/devices/register")
    suspend fun registerDevice(
        @Header("X-Client-App") clientApp: String = "employee",
        @Body request: RegisterDeviceRequest
    ): RegisterDeviceResponse
}