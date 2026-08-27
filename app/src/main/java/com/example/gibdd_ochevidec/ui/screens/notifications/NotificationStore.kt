package com.example.gibdd_ochevidec.ui.screens.notifications

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class NotificationStore private constructor(
    context: Context
) {

    private val preferences =
        context.applicationContext.getSharedPreferences(
            "employee_admin_notifications_v2",
            Context.MODE_PRIVATE
        )

    private val _notifications =
        MutableStateFlow(
            loadNotifications()
        )

    val notifications: StateFlow<List<NotificationItem>> =
        _notifications.asStateFlow()


    @Synchronized
    fun add(
        notification: NotificationItem
    ) {

        val current =
            _notifications.value

        // Не создаём дубль одного и того же события
        if (
            current.any {
                it.id == notification.id
            }
        ) {
            return
        }

        val updated =
            (
                    listOf(notification) +
                            current
                    )
                .take(100)

        _notifications.value =
            updated

        saveNotifications(
            updated
        )
    }


    private fun loadNotifications():
            List<NotificationItem> {

        val raw =
            preferences.getString(
                KEY_NOTIFICATIONS,
                null
            )
                ?: return emptyList()

        return runCatching {

            val array =
                JSONArray(raw)

            buildList {

                for (
                index in 0 until array.length()
                ) {

                    val item =
                        array.getJSONObject(
                            index
                        )

                    add(
                        NotificationItem(
                            id =
                                item.getString(
                                    "id"
                                ),

                            title =
                                item.getString(
                                    "title"
                                ),

                            text =
                                item.getString(
                                    "text"
                                ),

                            time =
                                item.getString(
                                    "time"
                                ),

                            observerDeviceId =
                                item
                                    .optString(
                                        "observerDeviceId"
                                    )
                                    .takeIf {
                                        it.isNotBlank()
                                    }
                        )
                    )
                }
            }

        }.getOrElse {
            emptyList()
        }
    }


    private fun saveNotifications(
        notifications:
        List<NotificationItem>
    ) {

        val array =
            JSONArray()

        notifications.forEach {
                notification ->

            array.put(
                JSONObject()
                    .put(
                        "id",
                        notification.id
                    )
                    .put(
                        "title",
                        notification.title
                    )
                    .put(
                        "text",
                        notification.text
                    )
                    .put(
                        "time",
                        notification.time
                    )

                    .put(
                        "observerDeviceId",
                        notification.observerDeviceId
                            ?: ""
                    )
            )
        }

        preferences
            .edit()
            .putString(
                KEY_NOTIFICATIONS,
                array.toString()
            )
            .apply()
    }


    companion object {

        private const val KEY_NOTIFICATIONS =
            "notifications"

        @Volatile
        private var instance:
                NotificationStore? = null


        fun getInstance(
            context: Context
        ): NotificationStore {

            return instance
                ?: synchronized(this) {

                    instance
                        ?: NotificationStore(
                            context
                        ).also {

                            instance =
                                it
                        }
                }
        }
    }
}