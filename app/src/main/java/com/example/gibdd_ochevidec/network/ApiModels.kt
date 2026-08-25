package com.example.gibdd_ochevidec.network

import com.google.gson.annotations.SerializedName

data class RegisterDeviceRequest(
    @SerializedName("fingerprint_hash") val fingerprintHash: String,
    @SerializedName("push_token") val pushToken: String? = null
)

data class RegisterDeviceResponse(
    @SerializedName("device_id") val deviceId: String,
    val role: String?,
    @SerializedName("access_token") val accessToken: String
)

data class HealthResponse(val status: String)
data class EmployeeMeResponse(@SerializedName("device_id") val deviceId: String, val role: String?)

data class ChatSummaryResponse(
    @SerializedName("observer_device_id") val observerDeviceId: String,
    @SerializedName("last_message_id") val lastMessageId: String,
    @SerializedName("last_message_type") val lastMessageType: String,
    @SerializedName("last_text") val lastText: String?,
    @SerializedName("last_created_at") val lastCreatedAt: String,
    @SerializedName("last_delivered_at") val lastDeliveredAt: String?,

    @SerializedName("last_media")
    val lastMedia: MediaResponse?,

    @SerializedName("active_ban")
    val activeBan: BanResponse?
)

data class ChatListResponse(val chats: List<ChatSummaryResponse>)

data class MessageResponse(
    @SerializedName("message_id") val messageId: String,
    @SerializedName("observer_device_id") val observerDeviceId: String,
    @SerializedName("sender_device_id") val senderDeviceId: String,
    @SerializedName("message_type") val messageType: String,
    val text: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("delivered_at") val deliveredAt: String?,

    @SerializedName("static_location")
    val staticLocation: StaticLocationResponse?,

    val media: MediaResponse?,

    @SerializedName("live_location")
    val liveLocation: LiveLocationResponse?,
    )

data class MessageListResponse(val messages: List<MessageResponse>)

data class SendMessageRequest(
    @SerializedName("message_type") val messageType: String = "TEXT",
    @SerializedName("observer_device_id") val observerDeviceId: String,
    val text: String
)

data class StaticLocationResponse(
    val latitude: Double,
    val longitude: Double
)

data class MediaResponse(
    @SerializedName("storage_key")
    val storageKey: String,

    @SerializedName("mime_type")
    val mimeType: String,

    @SerializedName("last_viewed_at")
    val lastViewedAt: String?
)

data class LiveLocationResponse(
    @SerializedName("ends_at")
    val endsAt: String?
)

data class LiveLocationPointResponse(
    @SerializedName("recorded_at")
    val recordedAt: String,

    val latitude: Double,

    val longitude: Double
)

data class LiveLocationPointsResponse(
    val points: List<LiveLocationPointResponse>
)

data class BanResponse(
    @SerializedName("ban_id")
    val banId: String,

    @SerializedName("observer_device_id")
    val observerDeviceId: String,

    @SerializedName("issued_by_device_id")
    val issuedByDeviceId: String,

    @SerializedName("started_at")
    val startedAt: String,

    @SerializedName("ends_at")
    val endsAt: String?,

    @SerializedName("ban_number")
    val banNumber: Int,

    @SerializedName("is_active")
    val isActive: Boolean
)

data class ActiveBanResponse(
    val ban: BanResponse?
)

data class EmployeeDeviceResponse(
    @SerializedName("device_id")
    val deviceId: String,

    val role: String?
)

data class RoleRequest(
    val role: String
)

data class RoleEventResponse(
    val action: String
)

data class RoleUpdateResponse(
    @SerializedName("device_id")
    val deviceId: String,

    val role: String,

    val event: RoleEventResponse?
)

data class EmployeeDeviceListItemResponse(
    @SerializedName("device_id")
    val deviceId: String,

    val role: String,

    @SerializedName("last_activity_at")
    val lastActivityAt: String?
)

data class EmployeeDevicesResponse(
    val devices: List<EmployeeDeviceListItemResponse>
)