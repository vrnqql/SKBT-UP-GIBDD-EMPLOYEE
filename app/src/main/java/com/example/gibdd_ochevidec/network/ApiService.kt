package com.example.gibdd_ochevidec.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Streaming
import retrofit2.http.DELETE
import retrofit2.http.PUT

interface ApiService {

    @DELETE("api/v1/employee/devices/{device_id}/role")
    suspend fun deleteEmployeeRole(
        @Path("device_id") deviceId: String,
        @Header("Authorization") authorization: String,
        @Header("X-Client-App") clientApp: String = "employee"
    ): Response<Unit>

    @Streaming
    @GET("api/v1/employee/reports/excel")
    suspend fun downloadExcelReport(
        @Header("Authorization")
        authorization: String,

        @Header("X-Client-App")
        clientApp: String = "employee"
    ): Response<ResponseBody>

    @GET("api/v1/employee/devices")
    suspend fun employeeDevices(
        @Header("Authorization")
        authorization: String,

        @Header("X-Client-App")
        clientApp: String = "employee"
    ): EmployeeDevicesResponse

    @PUT("api/v1/employee/devices/{device_id}/role")
    suspend fun updateEmployeeRole(
        @Path("device_id")
        deviceId: String,

        @Header("Authorization")
        authorization: String,

        @Header("X-Client-App")
        clientApp: String = "employee",

        @Body
        request: RoleRequest
    ): EmployeeDeviceResponse

    @GET("api/v1/employee/devices/{deviceId}")
    suspend fun employeeDevice(
        @Path("deviceId")
        deviceId: String,

        @Header("Authorization")
        authorization: String,

        @Header("X-Client-App")
        clientApp: String = "employee"
    ): EmployeeDeviceResponse

    @POST("api/v1/employee/devices/{deviceId}/ban")
    suspend fun banObserver(
        @Path("deviceId")
        deviceId: String,

        @Header("Authorization")
        authorization: String,

        @Header("X-Client-App")
        clientApp: String = "employee"
    ): BanResponse

    @GET("api/v1/employee/devices/{deviceId}/bans/active")
    suspend fun activeBan(
        @Path("deviceId")
        deviceId: String,

        @Header("Authorization")
        authorization: String,

        @Header("X-Client-App")
        clientApp: String = "employee"
    ): ActiveBanResponse

    @GET("api/v1/messages/{messageId}/live-location/points")
    suspend fun liveLocationPoints(
        @Path("messageId") messageId: String,

        @Header("Authorization")
        authorization: String,

        @Header("X-Client-App")
        clientApp: String = "employee",

        @Query("limit")
        limit: Int = 100
    ): LiveLocationPointsResponse

    @GET("health") suspend fun health(): HealthResponse

    @POST("api/v1/devices/register")
    suspend fun registerDevice(
        @Header("X-Client-App") clientApp: String = "employee",
        @Body request: RegisterDeviceRequest
    ): RegisterDeviceResponse

    @GET("api/v1/employee/me")
    suspend fun me(
        @Header("Authorization") authorization: String,
        @Header("X-Client-App") clientApp: String = "employee"
    ): EmployeeMeResponse

    @GET("api/v1/chats")
    suspend fun chats(
        @Header("Authorization") authorization: String,
        @Header("X-Client-App") clientApp: String = "employee"
    ): ChatListResponse

    @GET("api/v1/chats/{observerDeviceId}/messages")
    suspend fun messages(
        @Path("observerDeviceId") observerDeviceId: String,
        @Header("Authorization") authorization: String,
        @Header("X-Client-App") clientApp: String = "employee",
        @Query("after_message_id") afterMessageId: String? = null,
        @Query("limit") limit: Int = 100
    ): MessageListResponse

    @POST("api/v1/messages")
    suspend fun sendMessage(
        @Header("Authorization") authorization: String,
        @Header("X-Client-App") clientApp: String = "employee",
        @Body request: SendMessageRequest
    ): MessageResponse

    @PATCH("api/v1/messages/{messageId}/delivered")
    suspend fun markDelivered(
        @Path("messageId") messageId: String,
        @Header("Authorization") authorization: String,
        @Header("X-Client-App") clientApp: String = "employee"
    ): MessageResponse
}
