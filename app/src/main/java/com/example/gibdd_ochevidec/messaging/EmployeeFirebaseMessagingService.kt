package com.example.gibdd_ochevidec.messaging

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.gibdd_ochevidec.MainActivity
import com.example.gibdd_ochevidec.network.DeviceFingerprint
import com.example.gibdd_ochevidec.network.EmployeeSessionStore
import com.example.gibdd_ochevidec.network.RegisterDeviceRequest
import com.example.gibdd_ochevidec.network.RetrofitClient
import com.example.gibdd_ochevidec.ui.screens.notifications.NotificationItem
import com.example.gibdd_ochevidec.ui.screens.notifications.NotificationStore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EmployeeFirebaseMessagingService :
    FirebaseMessagingService() {


    override fun onMessageReceived(
        remoteMessage: RemoteMessage
    ) {

        super.onMessageReceived(
            remoteMessage
        )


        // =========================================
        // ДАННЫЕ ИЗ FCM
        // =========================================

        val data =
            remoteMessage.data


        val event =
            data["event"].orEmpty()


        val messageType =
            data["message_type"].orEmpty()


        val observerDeviceId =
            data["observer_device_id"].orEmpty()


        val action =
            data["action"].orEmpty()


        val issuedByDeviceId =
            data["issued_by_device_id"].orEmpty()


        val targetDeviceId =
            data["target_device_id"].orEmpty()


        val oldRole =
            data["old_role"]
                ?.takeIf {
                    it.isNotBlank() &&
                            it != "null"
                }


        val newRole =
            data["new_role"]
                ?.takeIf {
                    it.isNotBlank() &&
                            it != "null"
                }


        val createdAt =
            data["created_at"]


        val startedAt =
            data["started_at"]


        val endsAt =
            data["ends_at"]
                ?.takeIf {
                    it.isNotBlank() &&
                            it != "null"
                }


        val banNumber =
            data["ban_number"]
                ?.toIntOrNull()


        Log.d(
            "FCM",
            "Push получен: event=$event, messageType=$messageType"
        )


        // =========================================
        // ЗАГОЛОВОК PUSH
        // =========================================

        val notificationTitle =
            when (event) {

                "message_created" ->
                    "Новое сообщение от Очевидца"


                "observer_banned" ->
                    "Очевидец заблокирован"


                "role_changed" ->
                    when (action) {

                        "ASSIGNED" ->
                            "Роль назначена"

                        "REPLACED" ->
                            "Роль изменена"

                        "REMOVED" ->
                            "Роль удалена"

                        else ->
                            "Изменение роли"
                    }


                else ->
                    "ГИБДД"
            }


        // =========================================
        // ТЕКСТ PUSH
        // =========================================

        val notificationText =
            when (event) {

                // =================================
                // ОБЫЧНЫЕ СООБЩЕНИЯ
                // ВО ВКЛАДКУ "УВЕДОМЛЕНИЯ"
                // НЕ СОХРАНЯЮТСЯ
                // =================================

                "message_created" -> {

                    when (messageType) {

                        "TEXT" ->

                            remoteMessage
                                .notification
                                ?.body
                                ?.takeIf {
                                    it.isNotBlank()
                                }
                                ?: data["text"]
                                    ?.takeIf {
                                        it.isNotBlank()
                                    }
                                ?: "Очевидец отправил новое сообщение"


                        "MEDIA" ->
                            "Очевидец отправил фото или видео"


                        "STATIC_LOCATION" ->
                            "Очевидец отправил геопозицию"


                        "LIVE_LOCATION" ->
                            "Очевидец начал передачу геопозиции"


                        else ->
                            "Получено новое сообщение от Очевидца"
                    }
                }


                // =================================
                // ИЗМЕНЕНИЕ РОЛИ
                // =================================

                "role_changed" -> {

                    val manager =
                        shortDeviceId(
                            issuedByDeviceId
                        )


                    val target =
                        shortDeviceId(
                            targetDeviceId
                        )


                    when (action) {

                        "ASSIGNED" ->

                            "Сотрудник $manager назначил устройству " +
                                    "$target роль «${roleName(newRole)}»"


                        "REPLACED" ->

                            "Сотрудник $manager изменил роль устройства " +
                                    "$target: «${roleName(oldRole)}» → " +
                                    "«${roleName(newRole)}»"


                        "REMOVED" ->

                            "Сотрудник $manager удалил у устройства " +
                                    "$target роль «${roleName(oldRole)}»"


                        else ->

                            "Изменены данные роли устройства $target"
                    }
                }


                // =================================
                // БАН ОЧЕВИДЦА
                // =================================

                "observer_banned" -> {

                    val manager =
                        shortDeviceId(
                            issuedByDeviceId
                        )


                    val observer =
                        shortDeviceId(
                            observerDeviceId
                        )


                    val duration =
                        banDurationText(
                            banNumber = banNumber,
                            endsAt = endsAt
                        )


                    "Сотрудник $manager заблокировал " +
                            "Очевидца $observer $duration"
                }


                else ->
                    "Получено новое уведомление"
            }


        // =========================================
        // СОХРАНЕНИЕ ВО ВКЛАДКУ
        // "УВЕДОМЛЕНИЯ"
        //
        // ТОЛЬКО:
        // - РОЛИ
        // - БАНЫ
        // =========================================

        if (
            event == "observer_banned" ||
            event == "role_changed"
        ) {

            val eventId =
                when (event) {

                    "role_changed" ->
                        data["event_id"]


                    "observer_banned" ->
                        data["ban_id"]


                    else ->
                        null
                }
                    ?: "${event}_${System.currentTimeMillis()}"


            val eventTime =
                formatEventTime(
                    when (event) {

                        "role_changed" ->
                            createdAt


                        "observer_banned" ->
                            startedAt


                        else ->
                            null
                    }
                )


            NotificationStore
                .getInstance(
                    applicationContext
                )
                .add(
                    NotificationItem(
                        id =
                            eventId,

                        title =
                            notificationTitle,

                        text =
                            notificationText,

                        time =
                            eventTime,

                        observerDeviceId =
                            if (
                                event ==
                                "observer_banned"
                            ) {

                                observerDeviceId
                                    .takeIf {
                                        it.isNotBlank()
                                    }

                            } else {

                                null
                            }
                    )
                )
        }


        // =========================================
        // СИСТЕМНЫЙ ANDROID PUSH
        // =========================================

        showNotification(
            title =
                notificationTitle,

            text =
                notificationText,

            observerDeviceId =
                observerDeviceId
        )
    }


    // =========================================
    // НОВЫЙ FCM TOKEN
    // =========================================

    override fun onNewToken(
        token: String
    ) {

        super.onNewToken(
            token
        )


        Log.d(
            "FCM",
            "Получен новый FCM token"
        )


        CoroutineScope(
            Dispatchers.IO
        ).launch {

            try {

                val fingerprint =
                    DeviceFingerprint
                        .getFingerprintHash(
                            applicationContext
                        )


                val response =
                    RetrofitClient
                        .apiService
                        .registerDevice(
                            request =
                                RegisterDeviceRequest(
                                    fingerprintHash =
                                        fingerprint,

                                    pushToken =
                                        token
                                )
                        )


                EmployeeSessionStore(
                    applicationContext
                ).save(
                    response
                )


                Log.d(
                    "FCM",
                    "Новый push_token отправлен на backend"
                )


            } catch (
                error: Exception
            ) {

                Log.e(
                    "FCM",
                    "Не удалось обновить push_token на backend",
                    error
                )
            }
        }
    }


    // =========================================
    // КОРОТКИЙ ID УСТРОЙСТВА
    // =========================================

    private fun shortDeviceId(
        deviceId: String?
    ): String {

        if (
            deviceId.isNullOrBlank()
        ) {
            return "неизвестно"
        }


        return "•••${deviceId.takeLast(8)}"
    }


    // =========================================
    // НАЗВАНИЕ РОЛИ
    // =========================================

    private fun roleName(
        role: String?
    ): String {

        return when (role) {

            "INSPECTOR" ->
                "Инспектор"


            "ADMIN" ->
                "Администратор"


            "CHIEF" ->
                "Начальник"


            null ->
                "Без роли"


            else ->
                role
        }
    }


    // =========================================
    // СРОК БАНА
    // =========================================

    private fun banDurationText(
        banNumber: Int?,
        endsAt: String?
    ): String {

        // ends_at = null означает
        // бессрочную блокировку
        if (
            endsAt.isNullOrBlank()
        ) {
            return "бессрочно"
        }


        return when (banNumber) {

            1 ->
                "на 24 часа"


            2 ->
                "на 30 дней"


            else ->
                "до ${formatEventDateTime(endsAt)}"
        }
    }


    // =========================================
    // ВРЕМЯ ДЛЯ КАРТОЧКИ
    // =========================================

    private fun formatEventTime(
        value: String?
    ): String {

        if (
            value.isNullOrBlank()
        ) {

            return SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(
                Date()
            )
        }


        return runCatching {

            java.time.OffsetDateTime
                .parse(
                    value
                )
                .atZoneSameInstant(
                    java.time.ZoneId
                        .systemDefault()
                )
                .format(
                    java.time.format.DateTimeFormatter
                        .ofPattern(
                            "HH:mm"
                        )
                )

        }.getOrElse {

            SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(
                Date()
            )
        }
    }


    // =========================================
    // ДАТА + ВРЕМЯ
    // =========================================

    private fun formatEventDateTime(
        value: String
    ): String {

        return runCatching {

            java.time.OffsetDateTime
                .parse(
                    value
                )
                .atZoneSameInstant(
                    java.time.ZoneId
                        .systemDefault()
                )
                .format(
                    java.time.format.DateTimeFormatter
                        .ofPattern(
                            "dd.MM.yyyy HH:mm"
                        )
                )

        }.getOrElse {
            value
        }
    }


    // =========================================
    // ПОКАЗ СИСТЕМНОГО УВЕДОМЛЕНИЯ
    // =========================================

    private fun showNotification(
        title: String,
        text: String,
        observerDeviceId: String
    ) {

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            Log.w(
                "FCM",
                "Нет разрешения POST_NOTIFICATIONS"
            )

            return
        }


        val intent =
            Intent(
                this,
                MainActivity::class.java
            ).apply {

                flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP


                if (
                    observerDeviceId.isNotBlank()
                ) {

                    putExtra(
                        "observer_device_id",
                        observerDeviceId
                    )
                }
            }


        val notificationId =
            (
                    System.currentTimeMillis() %
                            Int.MAX_VALUE
                    ).toInt()


        val pendingIntent =
            PendingIntent.getActivity(
                this,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )


        val notification =
            NotificationCompat
                .Builder(
                    this,
                    MainActivity.NOTIFICATION_CHANNEL_ID
                )
                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )
                .setContentTitle(
                    title
                )
                .setContentText(
                    text
                )
                .setStyle(
                    NotificationCompat
                        .BigTextStyle()
                        .bigText(
                            text
                        )
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(
                    true
                )
                .setContentIntent(
                    pendingIntent
                )
                .build()


        NotificationManagerCompat
            .from(
                this
            )
            .notify(
                notificationId,
                notification
            )
    }
}