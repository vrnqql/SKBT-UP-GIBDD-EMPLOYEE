package com.example.gibdd_ochevidec

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gibdd_ochevidec.network.DeviceFingerprint
import com.example.gibdd_ochevidec.network.EmployeeDeviceResponse
import com.example.gibdd_ochevidec.network.EmployeeSession
import com.example.gibdd_ochevidec.network.EmployeeSessionStore
import com.example.gibdd_ochevidec.network.MessageResponse
import com.example.gibdd_ochevidec.network.RegisterDeviceRequest
import com.example.gibdd_ochevidec.network.RetrofitClient
import com.example.gibdd_ochevidec.network.RoleRequest
import com.example.gibdd_ochevidec.network.SendMessageRequest
import com.example.gibdd_ochevidec.ui.screens.chat.ChatItem
import com.example.gibdd_ochevidec.ui.screens.chat.ChatMessageUi
import com.example.gibdd_ochevidec.ui.screens.chat.ChatScreen
import com.example.gibdd_ochevidec.ui.screens.chat.ChatsScreen
import com.example.gibdd_ochevidec.ui.screens.common.LoadingScreen
import com.example.gibdd_ochevidec.ui.screens.common.WaitingRoleScreen
import com.example.gibdd_ochevidec.ui.screens.employees.DeviceNotRegisteredScreen
import com.example.gibdd_ochevidec.ui.screens.employees.EmployeeDeviceScreen
import com.example.gibdd_ochevidec.ui.screens.employees.EmployeeItem
import com.example.gibdd_ochevidec.ui.screens.employees.EmployeesScreen
import com.example.gibdd_ochevidec.ui.screens.employees.InvalidQrScreen
import com.example.gibdd_ochevidec.ui.screens.employees.QrScannerScreen
import com.example.gibdd_ochevidec.ui.screens.employees.ReplaceRoleConfirmationScreen
import com.example.gibdd_ochevidec.ui.screens.employees.RoleSelectionScreen
import com.example.gibdd_ochevidec.ui.screens.employees.RoleSuccessScreen
import com.example.gibdd_ochevidec.ui.screens.reports.ReportsScreen
import com.example.gibdd_ochevidec.ui.theme.GIBDD_OchevidecTheme
import com.example.gibdd_ochevidec.utils.formatChatListDateTime
import com.example.gibdd_ochevidec.utils.formatMessageTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.gibdd_ochevidec.ui.screens.reports.ReportScreenState
import com.example.gibdd_ochevidec.ui.screens.notifications.NotificationItem
import com.example.gibdd_ochevidec.ui.screens.notifications.NotificationsScreen
import com.example.gibdd_ochevidec.ui.screens.employees.DeleteRoleConfirmationScreen
import com.example.gibdd_ochevidec.ui.screens.employees.RoleErrorScreen


private enum class AppScreen {

    LOADING,
    ERROR,
    WAITING_ROLE,

    CHATS,
    CHAT,

    EMPLOYEES,
    NOTIFICATIONS,
    REPORTS,

    QR_SCANNER,
    EMPLOYEE_DEVICE,
    DEVICE_NOT_REGISTERED,
    INVALID_QR,

    ROLE_SELECTION,
    ROLE_REPLACE_CONFIRM,
    ROLE_SUCCESS,
    ROLE_DELETE_CONFIRM,
    ROLE_ERROR,
}


class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()


        setContent {

            GIBDD_OchevidecTheme {

                var roleFailureReason by remember {
                    mutableStateOf(
                        "Устройство не зарегистрировано"
                    )
                }

                var deleteRoleLoading by remember {
                    mutableStateOf(false)
                }

                var deleteRoleErrorText by remember {
                    mutableStateOf<String?>(null)
                }

                var notifications by remember {
                    mutableStateOf<List<NotificationItem>>(
                        emptyList()
                    )
                }

                val scope =
                    rememberCoroutineScope()

                val context =
                    LocalContext.current


                // =========================================
                // ОСНОВНЫЕ СОСТОЯНИЯ
                // =========================================

                var currentScreen by remember {
                    mutableStateOf(
                        AppScreen.LOADING
                    )
                }


                var session by remember {
                    mutableStateOf<EmployeeSession?>(
                        null
                    )
                }


                var registrationAttempt by remember {
                    mutableIntStateOf(0)
                }


                var errorText by remember {
                    mutableStateOf("")
                }


                val sessionStore =
                    remember {

                        EmployeeSessionStore(
                            applicationContext
                        )
                    }


                // =========================================
                // ЧАТЫ
                // =========================================

                var selectedChatId by remember {
                    mutableStateOf("")
                }


                var selectedChatName by remember {
                    mutableStateOf("")
                }


                var serverChats by remember {
                    mutableStateOf<List<ChatItem>>(
                        emptyList()
                    )
                }


                var chatMessages by remember {
                    mutableStateOf<List<ChatMessageUi>>(
                        emptyList()
                    )
                }


                val aliasPreferences =
                    remember {

                        getSharedPreferences(
                            "chat_aliases",
                            MODE_PRIVATE
                        )
                    }


                var aliases by remember {

                    mutableStateOf(

                        aliasPreferences
                            .all
                            .filterKeys {
                                it.startsWith(
                                    "alias_"
                                )
                            }
                            .mapKeys {
                                it.key.removePrefix(
                                    "alias_"
                                )
                            }
                            .mapValues {
                                it.value.toString()
                            }
                    )
                }


                // =========================================
                // СОТРУДНИКИ
                // =========================================

                var employees by remember {
                    mutableStateOf<List<EmployeeItem>>(
                        emptyList()
                    )
                }


                var scannedEmployeeDevice by remember {
                    mutableStateOf<EmployeeDeviceResponse?>(
                        null
                    )
                }


                // =========================================
                // РОЛИ
                // =========================================

                var selectedNewRole by remember {
                    mutableStateOf<String?>(
                        null
                    )
                }


                var roleErrorText by remember {
                    mutableStateOf<String?>(
                        null
                    )
                }


                var roleRequestLoading by remember {
                    mutableStateOf(false)
                }


                var reportScreenState by remember {
                    mutableStateOf(
                        ReportScreenState.MAIN
                    )
                }

                var reportErrorText by remember {
                    mutableStateOf<String?>(null)
                }

                var reportFileBytes by remember {
                    mutableStateOf<ByteArray?>(null)
                }

                LaunchedEffect(
                    registrationAttempt
                ) {

                    currentScreen =
                        AppScreen.LOADING


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
                                                fingerprint
                                        )
                                )


                        sessionStore.save(
                            response
                        )


                        session =
                            EmployeeSession(
                                deviceId =
                                    response.deviceId,

                                role =
                                    response.role,

                                accessToken =
                                    response.accessToken
                            )


                        currentScreen =
                            if (
                                response.role == null
                            ) {

                                AppScreen.WAITING_ROLE

                            } else {

                                AppScreen.CHATS
                            }


                    } catch (
                        error: Exception
                    ) {

                        session =
                            sessionStore.load()


                        errorText =
                            error.message
                                ?: "Не удалось подключиться к серверу"


                        currentScreen =
                            AppScreen.ERROR
                    }
                }


                // =========================================
                // ОЖИДАНИЕ РОЛИ
                // =========================================

                LaunchedEffect(
                    currentScreen,
                    session?.accessToken
                ) {

                    val activeSession =
                        session


                    if (
                        currentScreen ==
                        AppScreen.WAITING_ROLE &&
                        activeSession != null
                    ) {

                        while (true) {

                            delay(5_000)


                            try {

                                val me =
                                    RetrofitClient
                                        .apiService
                                        .me(
                                            authorization =
                                                activeSession.authorization
                                        )


                                if (
                                    me.role != null
                                ) {

                                    session =
                                        activeSession.copy(
                                            role = me.role
                                        )


                                    currentScreen =
                                        AppScreen.CHATS

                                    break
                                }


                            } catch (
                                error: Exception
                            ) {

                                if (
                                    (
                                            error as?
                                                    HttpException
                                            )
                                        ?.code() == 401
                                ) {

                                    registrationAttempt++

                                    break
                                }
                            }
                        }
                    }
                }


                // =========================================
                // СПИСОК ЧАТОВ
                // =========================================

                LaunchedEffect(
                    currentScreen,
                    session?.accessToken
                ) {

                    val activeSession =
                        session


                    if (
                        currentScreen ==
                        AppScreen.CHATS &&
                        activeSession != null
                    ) {

                        while (true) {

                            try {

                                val response =
                                    RetrofitClient
                                        .apiService
                                        .chats(
                                            activeSession.authorization
                                        )


                                serverChats =
                                    response.chats.map {
                                            summary ->


                                        ChatItem(

                                            id =
                                                summary.observerDeviceId,


                                            name =
                                                "Очевидец ${
                                                    summary
                                                        .observerDeviceId
                                                        .take(8)
                                                }",


                                            message =
                                                when (
                                                    summary
                                                        .lastMessageType
                                                ) {

                                                    "MEDIA" -> {

                                                        if (
                                                            summary
                                                                .lastMedia
                                                                ?.mimeType
                                                                ?.startsWith(
                                                                    "video/"
                                                                ) == true
                                                        ) {

                                                            "Видео"

                                                        } else {

                                                            "Фото"
                                                        }
                                                    }


                                                    "STATIC_LOCATION" ->
                                                        "Геопозиция"


                                                    "LIVE_LOCATION" ->
                                                        "Геопозиция в реальном времени"


                                                    else ->
                                                        summary.lastText
                                                            ?: summary
                                                                .lastMessageType
                                                },


                                            time =
                                                formatChatListDateTime(
                                                    summary
                                                        .lastCreatedAt
                                                ),


                                            unreadCount =
                                                if (
                                                    summary
                                                        .lastDeliveredAt ==
                                                    null
                                                ) {
                                                    1
                                                } else {
                                                    0
                                                },


                                            isBanned =
                                                summary
                                                    .activeBan
                                                    ?.isActive ==
                                                        true
                                        )
                                    }


                            } catch (
                                error: Exception
                            ) {

                                if (
                                    (
                                            error as?
                                                    HttpException
                                            )
                                        ?.code() == 401
                                ) {

                                    registrationAttempt++

                                    break
                                }
                            }


                            delay(5_000)
                        }
                    }
                }


                // =========================================
                // СООБЩЕНИЯ ОТКРЫТОГО ЧАТА
                // =========================================

                LaunchedEffect(
                    currentScreen,
                    selectedChatId,
                    session?.deviceId
                ) {

                    val activeSession =
                        session


                    if (
                        currentScreen ==
                        AppScreen.CHAT &&
                        activeSession != null &&
                        selectedChatId.isNotBlank()
                    ) {

                        var showError =
                            true


                        while (true) {

                            try {

                                val response =
                                    RetrofitClient
                                        .apiService
                                        .messages(

                                            observerDeviceId =
                                                selectedChatId,

                                            authorization =
                                                activeSession
                                                    .authorization
                                        )


                                val loaded =
                                    response
                                        .messages
                                        .map {

                                            it.toUi(
                                                activeSession.deviceId
                                            )
                                        }


                                chatMessages =
                                    (
                                            chatMessages +
                                                    loaded
                                            )
                                        .distinctBy {
                                            it.id
                                        }


                                response
                                    .messages
                                    .filter {

                                        it.senderDeviceId !=
                                                activeSession.deviceId &&

                                                it.deliveredAt ==
                                                null
                                    }
                                    .forEach {
                                            message ->


                                        runCatching {

                                            RetrofitClient
                                                .apiService
                                                .markDelivered(

                                                    messageId =
                                                        message.messageId,

                                                    authorization =
                                                        activeSession
                                                            .authorization
                                                )
                                        }
                                    }


                                showError =
                                    false


                            } catch (
                                error: Exception
                            ) {

                                if (showError) {

                                    Toast
                                        .makeText(
                                            applicationContext,

                                            error.userMessage(
                                                "Не удалось загрузить сообщения"
                                            ),

                                            Toast.LENGTH_LONG
                                        )
                                        .show()


                                    showError =
                                        false
                                }
                            }


                            delay(5_000)
                        }
                    }
                }


                // =========================================
                // СПИСОК СОТРУДНИКОВ
                // =========================================

                LaunchedEffect(
                    currentScreen,
                    session?.accessToken
                ) {

                    val activeSession =
                        session


                    if (
                        currentScreen ==
                        AppScreen.EMPLOYEES &&
                        activeSession != null &&
                        (
                                activeSession.role ==
                                        "ADMIN" ||
                                        activeSession.role ==
                                        "CHIEF"
                                )
                    ) {

                        try {

                            val response =
                                RetrofitClient
                                    .apiService
                                    .employeeDevices(

                                        authorization =
                                            activeSession.authorization
                                    )


                            employees =
                                response
                                    .devices
                                    .map {
                                            device ->


                                        EmployeeItem(

                                            deviceId =
                                                device.deviceId,


                                            name =
                                                null,


                                            role =
                                                device.role,


                                            lastActivity =
                                                device
                                                    .lastActivityAt
                                                    ?.let {

                                                        formatChatListDateTime(
                                                            it
                                                        )
                                                    }
                                        )
                                    }


                        } catch (
                            error: Exception
                        ) {

                            Toast
                                .makeText(
                                    applicationContext,

                                    error.userMessage(
                                        "Не удалось загрузить сотрудников"
                                    ),

                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                }


                // =========================================
                // НАВИГАЦИЯ
                // =========================================

                when (
                    currentScreen
                ) {


                    AppScreen.LOADING -> {

                        LoadingScreen()
                    }


                    AppScreen.ERROR -> {

                        ConnectionError(
                            message =
                                errorText,

                            onRetry = {
                                registrationAttempt++
                            }
                        )
                    }


                    AppScreen.WAITING_ROLE -> {

                        WaitingRoleScreen(
                            deviceId =
                                session
                                    ?.deviceId
                                    .orEmpty()
                        )
                    }


                    // =====================================
                    // ЧАТЫ
                    // =====================================

                    AppScreen.CHATS -> {

                        ChatsScreen(

                            chats = serverChats,

                            aliases = aliases,


                            showEmployeesTab =
                                session?.role == "ADMIN" ||
                                        session?.role == "CHIEF",


                            showNotificationsTab =
                                session?.role == "CHIEF",


                            showReportsTab =
                                session?.role == "CHIEF",


                            onEmployeesClick = {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            },


                            onNotificationsClick = {

                                currentScreen =
                                    AppScreen.NOTIFICATIONS
                            },


                            onReportsClick = {

                                currentScreen =
                                    AppScreen.REPORTS
                            },


                            onChatClick = { id, name ->

                                selectedChatId =
                                    id

                                selectedChatName =
                                    name

                                chatMessages =
                                    emptyList()

                                currentScreen =
                                    AppScreen.CHAT
                            }
                        )
                    }


                    // =====================================
                    // ОТКРЫТЫЙ ЧАТ
                    // =====================================

                    AppScreen.CHAT -> {

                        ChatScreen(

                            chatId =
                                selectedChatId,


                            chatName =
                                selectedChatName,


                            messages =
                                chatMessages,


                            authorization =
                                session
                                    ?.authorization
                                    .orEmpty(),


                            onBackClick = {

                                currentScreen =
                                    AppScreen.CHATS
                            },


                            onAliasChanged = {
                                    newAlias ->


                                aliasPreferences
                                    .edit()
                                    .putString(
                                        "alias_$selectedChatId",
                                        newAlias
                                    )
                                    .apply()


                                aliases =
                                    aliases +
                                            (
                                                    selectedChatId
                                                            to
                                                            newAlias
                                                    )


                                selectedChatName =
                                    newAlias
                            },


                            onBlockConfirmed = {

                                val activeSession =
                                    session


                                if (
                                    activeSession != null &&
                                    selectedChatId
                                        .isNotBlank()
                                ) {

                                    scope.launch {

                                        try {

                                            RetrofitClient
                                                .apiService
                                                .banObserver(

                                                    deviceId =
                                                        selectedChatId,

                                                    authorization =
                                                        activeSession
                                                            .authorization
                                                )


                                            currentScreen =
                                                AppScreen.CHATS


                                        } catch (
                                            error: Exception
                                        ) {

                                            Toast
                                                .makeText(
                                                    applicationContext,

                                                    error.userMessage(
                                                        "Не удалось заблокировать очевидца"
                                                    ),

                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                }
                            },


                            onTemplateSend = {
                                    text ->


                                val activeSession =
                                    session


                                if (
                                    activeSession != null
                                ) {

                                    scope.launch {

                                        try {

                                            val sent =
                                                RetrofitClient
                                                    .apiService
                                                    .sendMessage(

                                                        authorization =
                                                            activeSession
                                                                .authorization,

                                                        request =
                                                            SendMessageRequest(

                                                                observerDeviceId =
                                                                    selectedChatId,

                                                                text =
                                                                    text
                                                            )
                                                    )


                                            if (
                                                chatMessages
                                                    .none {
                                                        it.id ==
                                                                sent.messageId
                                                    }
                                            ) {

                                                chatMessages =
                                                    chatMessages +
                                                            sent.toUi(
                                                                activeSession
                                                                    .deviceId
                                                            )
                                            }


                                        } catch (
                                            error: Exception
                                        ) {

                                            Toast
                                                .makeText(
                                                    applicationContext,

                                                    error.userMessage(
                                                        "Сообщение не отправлено"
                                                    ),

                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                }
                            }
                        )
                    }


                    // =====================================
                    // СОТРУДНИКИ
                    // =====================================

                    AppScreen.EMPLOYEES -> {

                        val role =
                            session?.role


                        if (
                            role == "ADMIN" ||
                            role == "CHIEF"
                        ) {

                            EmployeesScreen(

                                employees =
                                    employees,


                                showNotificationsTab =
                                    role == "CHIEF",


                                showReportsTab =
                                    role == "CHIEF",


                                onChatsClick = {

                                    currentScreen =
                                        AppScreen.CHATS
                                },


                                onNotificationsClick = {

                                    currentScreen =
                                        AppScreen.NOTIFICATIONS
                                },


                                onReportsClick = {

                                    currentScreen =
                                        AppScreen.REPORTS
                                },


                                onAddEmployeeClick = {

                                    currentScreen =
                                        AppScreen.QR_SCANNER
                                },


                                onEmployeeClick = {
                                        employee ->


                                    scannedEmployeeDevice =
                                        EmployeeDeviceResponse(

                                            deviceId =
                                                employee.deviceId,

                                            role =
                                                employee.role
                                        )


                                    currentScreen =
                                        AppScreen.EMPLOYEE_DEVICE
                                }
                            )


                        } else {

                            LaunchedEffect(
                                role
                            ) {

                                currentScreen =
                                    AppScreen.CHATS
                            }
                        }
                    }


                    // =====================================
                    // УВЕДОМЛЕНИЯ
                    // ПОКА ВЕРСТКУ ЕЩЁ НЕ ПОДКЛЮЧАЛИ
                    // =====================================

                    AppScreen.NOTIFICATIONS -> {

                        if (session?.role == "CHIEF") {

                            NotificationsScreen(

                                notifications =
                                    notifications,


                                onChatsClick = {

                                    currentScreen =
                                        AppScreen.CHATS
                                },


                                onEmployeesClick = {

                                    currentScreen =
                                        AppScreen.EMPLOYEES
                                },


                                onReportsClick = {

                                    currentScreen =
                                        AppScreen.REPORTS
                                }
                            )


                        } else {

                            LaunchedEffect(Unit) {

                                currentScreen =
                                    AppScreen.CHATS
                            }
                        }
                    }

                    AppScreen.QR_SCANNER -> {

                        QrScannerScreen(

                            onBackClick = {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            },


                            onInvalidQr = {

                                currentScreen =
                                    AppScreen.INVALID_QR
                            },


                            onQrScanned = {
                                    deviceId ->


                                val activeSession =
                                    session


                                if (
                                    activeSession != null
                                ) {

                                    scope.launch {

                                        try {

                                            val device =
                                                RetrofitClient
                                                    .apiService
                                                    .employeeDevice(

                                                        deviceId =
                                                            deviceId,

                                                        authorization =
                                                            activeSession
                                                                .authorization
                                                    )


                                            scannedEmployeeDevice =
                                                device


                                            currentScreen =
                                                AppScreen.EMPLOYEE_DEVICE


                                        } catch (
                                            error: HttpException
                                        ) {

                                            if (
                                                error.code() ==
                                                404
                                            ) {

                                                scannedEmployeeDevice =
                                                    null


                                                currentScreen =
                                                    AppScreen
                                                        .DEVICE_NOT_REGISTERED


                                            } else {

                                                Toast
                                                    .makeText(
                                                        applicationContext,

                                                        error.userMessage(
                                                            "Не удалось проверить устройство"
                                                        ),

                                                        Toast.LENGTH_LONG
                                                    )
                                                    .show()
                                            }


                                        } catch (
                                            error: Exception
                                        ) {

                                            Toast
                                                .makeText(
                                                    applicationContext,

                                                    error.userMessage(
                                                        "Не удалось проверить устройство"
                                                    ),

                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                }
                            }
                        )
                    }


                    AppScreen.INVALID_QR -> {

                        InvalidQrScreen(

                            onRetryClick = {

                                currentScreen =
                                    AppScreen.QR_SCANNER
                            },


                            onBackClick = {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            }
                        )
                    }


                    AppScreen.DEVICE_NOT_REGISTERED -> {

                        DeviceNotRegisteredScreen(

                            onRetryClick = {

                                currentScreen =
                                    AppScreen.QR_SCANNER
                            },


                            onBackClick = {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            }
                        )
                    }


                    // =====================================
                    // УСТРОЙСТВО СОТРУДНИКА
                    // =====================================

                    AppScreen.EMPLOYEE_DEVICE -> {

                        val device =
                            scannedEmployeeDevice


                        if (
                            device != null
                        ) {

                            EmployeeDeviceScreen(

                                onDeleteRoleClick = {

                                    deleteRoleErrorText = null

                                    currentScreen =
                                        AppScreen.ROLE_DELETE_CONFIRM
                                },

                                deviceId =
                                    device.deviceId,


                                role =
                                    device.role,


                                onBackClick = {

                                    currentScreen =
                                        AppScreen.EMPLOYEES
                                },


                                onRoleClick = {

                                    roleErrorText =
                                        null


                                    currentScreen =
                                        AppScreen.ROLE_SELECTION
                                }
                            )


                        } else {

                            LaunchedEffect(Unit) {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            }
                        }
                    }


                    // =====================================
                    // ВЫБОР РОЛИ
                    // =====================================

                    AppScreen.ROLE_SELECTION -> {

                        val device =
                            scannedEmployeeDevice


                        if (
                            device != null
                        ) {

                            RoleSelectionScreen(

                                deviceId =
                                    device.deviceId,


                                currentRole =
                                    device.role,


                                managerRole =
                                    session
                                        ?.role
                                        .orEmpty(),


                                errorText =
                                    roleErrorText,


                                isLoading =
                                    roleRequestLoading,


                                onBackClick = {

                                    roleErrorText =
                                        null


                                    currentScreen =
                                        AppScreen.EMPLOYEE_DEVICE
                                },


                                onRoleSelected = {
                                        role ->


                                    selectedNewRole =
                                        role


                                    roleErrorText =
                                        null


                                    if (
                                        device.role != null
                                    ) {

                                        currentScreen =
                                            AppScreen
                                                .ROLE_REPLACE_CONFIRM


                                    } else {

                                        val activeSession =
                                            session


                                        if (
                                            activeSession != null
                                        ) {

                                            roleRequestLoading =
                                                true


                                            scope.launch {

                                                try {

                                                    val result =
                                                        RetrofitClient
                                                            .apiService
                                                            .updateEmployeeRole(

                                                                deviceId =
                                                                    device
                                                                        .deviceId,

                                                                authorization =
                                                                    activeSession
                                                                        .authorization,

                                                                request =
                                                                    RoleRequest(
                                                                        role =
                                                                            role
                                                                    )
                                                            )


                                                    scannedEmployeeDevice =
                                                        EmployeeDeviceResponse(

                                                            deviceId =
                                                                result.deviceId,

                                                            role =
                                                                result.role
                                                        )


                                                    roleRequestLoading =
                                                        false


                                                    currentScreen =
                                                        AppScreen.ROLE_SUCCESS


                                                } catch (
                                                    error: Exception
                                                ) {

                                                    roleRequestLoading =
                                                        false


                                                    roleFailureReason =
                                                        if (
                                                            (error as? HttpException)
                                                                ?.code() == 404
                                                        ) {

                                                            "Устройство не зарегистрировано"

                                                        } else {

                                                            error.userMessage(
                                                                "Ошибка сервера"
                                                            )
                                                        }


                                                    currentScreen =
                                                        AppScreen.ROLE_ERROR
                                                }
                                            }
                                        }
                                    }
                                }
                            )


                        } else {

                            LaunchedEffect(Unit) {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            }
                        }
                    }


                    // =====================================
                    // ПОДТВЕРЖДЕНИЕ ЗАМЕНЫ
                    // =====================================

                    AppScreen.ROLE_REPLACE_CONFIRM -> {

                        val device =
                            scannedEmployeeDevice


                        val newRole =
                            selectedNewRole


                        if (
                            device != null &&
                            device.role != null &&
                            newRole != null
                        ) {

                            ReplaceRoleConfirmationScreen(

                                deviceId =
                                    device.deviceId,


                                oldRole =
                                    device.role,


                                newRole =
                                    newRole,


                                errorText =
                                    roleErrorText,


                                isLoading =
                                    roleRequestLoading,


                                onCancel = {

                                    roleErrorText =
                                        null


                                    currentScreen =
                                        AppScreen.ROLE_SELECTION
                                },


                                onConfirm = {

                                    val activeSession =
                                        session


                                    if (
                                        activeSession != null
                                    ) {

                                        roleRequestLoading =
                                            true


                                        roleErrorText =
                                            null


                                        scope.launch {

                                            try {

                                                val result =
                                                    RetrofitClient
                                                        .apiService
                                                        .updateEmployeeRole(

                                                            deviceId =
                                                                device.deviceId,

                                                            authorization =
                                                                activeSession
                                                                    .authorization,

                                                            request =
                                                                RoleRequest(
                                                                    role =
                                                                        newRole
                                                                )
                                                        )


                                                scannedEmployeeDevice =
                                                    EmployeeDeviceResponse(

                                                        deviceId =
                                                            result.deviceId,

                                                        role =
                                                            result.role
                                                    )


                                                roleRequestLoading =
                                                    false


                                                currentScreen =
                                                    AppScreen.ROLE_SUCCESS


                                            } catch (
                                                error: Exception
                                            ) {

                                                roleRequestLoading =
                                                    false


                                                roleFailureReason =
                                                    if (
                                                        (error as? HttpException)
                                                            ?.code() == 404
                                                    ) {

                                                        "Устройство не зарегистрировано"

                                                    } else {

                                                        error.userMessage(
                                                            "Ошибка сервера"
                                                        )
                                                    }


                                                currentScreen =
                                                    AppScreen.ROLE_ERROR
                                            }
                                        }
                                    }
                                }
                            )


                        } else {

                            LaunchedEffect(Unit) {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            }
                        }
                    }

                    AppScreen.ROLE_DELETE_CONFIRM -> {

                        val device =
                            scannedEmployeeDevice

                        val currentRole =
                            device?.role


                        if (
                            device != null &&
                            currentRole != null
                        ) {

                            DeleteRoleConfirmationScreen(

                                role =
                                    currentRole,

                                isLoading =
                                    deleteRoleLoading,

                                errorText =
                                    deleteRoleErrorText,


                                onBackClick = {

                                    deleteRoleErrorText = null

                                    currentScreen =
                                        AppScreen.EMPLOYEE_DEVICE
                                },


                                onCancelClick = {

                                    deleteRoleErrorText = null

                                    currentScreen =
                                        AppScreen.EMPLOYEE_DEVICE
                                },


                                onConfirmClick = {

                                    val activeSession =
                                        session


                                    if (
                                        activeSession != null &&
                                        !deleteRoleLoading
                                    ) {

                                        deleteRoleLoading = true
                                        deleteRoleErrorText = null


                                        scope.launch {

                                            try {

                                                val response =
                                                    RetrofitClient
                                                        .apiService
                                                        .deleteEmployeeRole(

                                                            deviceId =
                                                                device.deviceId,

                                                            authorization =
                                                                activeSession
                                                                    .authorization
                                                        )


                                                if (!response.isSuccessful) {

                                                    error(
                                                        "Ошибка сервера: ${
                                                            response.code()
                                                        }"
                                                    )
                                                }


                                                // Локально тоже обновляем роль
                                                scannedEmployeeDevice =
                                                    EmployeeDeviceResponse(
                                                        deviceId =
                                                            device.deviceId,

                                                        role = null
                                                    )


                                                // Обновляем список сотрудников
                                                employees =
                                                    employees.filterNot {
                                                        it.deviceId ==
                                                                device.deviceId
                                                    }


                                                deleteRoleLoading = false
                                                deleteRoleErrorText = null


                                                Toast
                                                    .makeText(
                                                        applicationContext,
                                                        "Роль удалена",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()


                                                currentScreen =
                                                    AppScreen.EMPLOYEES


                                            } catch (
                                                error: Exception
                                            ) {

                                                deleteRoleLoading = false

                                                deleteRoleErrorText =
                                                    error.userMessage(
                                                        "Не удалось удалить роль"
                                                    )
                                            }
                                        }
                                    }
                                }
                            )


                        } else {

                            LaunchedEffect(Unit) {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            }
                        }
                    }


                    // =====================================
                    // УСПЕШНОЕ НАЗНАЧЕНИЕ
                    // =====================================

                    AppScreen.ROLE_SUCCESS -> {

                        val device =
                            scannedEmployeeDevice


                        if (
                            device != null &&
                            device.role != null
                        ) {

                            RoleSuccessScreen(

                                deviceId =
                                    device.deviceId,


                                role =
                                    device.role,


                                onDoneClick = {

                                    selectedNewRole =
                                        null


                                    roleErrorText =
                                        null


                                    currentScreen =
                                        AppScreen.EMPLOYEES
                                }
                            )


                        } else {

                            LaunchedEffect(Unit) {

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            }
                        }
                    }

                    AppScreen.ROLE_ERROR -> {

                        RoleErrorScreen(

                            reason =
                                roleFailureReason,


                            onRetryScanClick = {

                                roleErrorText = null

                                selectedNewRole = null

                                scannedEmployeeDevice = null

                                currentScreen =
                                    AppScreen.QR_SCANNER
                            },


                            onCloseClick = {

                                roleErrorText = null

                                selectedNewRole = null

                                scannedEmployeeDevice = null

                                currentScreen =
                                    AppScreen.EMPLOYEES
                            }
                        )
                    }

                    AppScreen.REPORTS -> {

                        if (session?.role == "CHIEF") {

                            ReportsScreen(

                                state =
                                    reportScreenState,

                                errorText =
                                    reportErrorText,


                                // =====================================
                                // НАЖАЛИ "СКАЧАТЬ ОТЧЁТ"
                                // НА ГЛАВНОМ ЭКРАНЕ
                                // =====================================

                                onGenerateClick = {

                                    val activeSession =
                                        session


                                    if (activeSession != null) {

                                        reportScreenState =
                                            ReportScreenState.LOADING

                                        reportErrorText =
                                            null

                                        reportFileBytes =
                                            null


                                        scope.launch {

                                            try {

                                                val response =
                                                    RetrofitClient
                                                        .apiService
                                                        .downloadExcelReport(

                                                            authorization =
                                                                activeSession
                                                                    .authorization
                                                        )


                                                if (!response.isSuccessful) {

                                                    error(
                                                        "Ошибка сервера: ${
                                                            response.code()
                                                        }"
                                                    )
                                                }


                                                val body =
                                                    response.body()
                                                        ?: error(
                                                            "Сервер вернул пустой файл"
                                                        )


                                                val bytes =
                                                    withContext(
                                                        Dispatchers.IO
                                                    ) {

                                                        body.bytes()
                                                    }


                                                reportFileBytes =
                                                    bytes


                                                reportScreenState =
                                                    ReportScreenState.READY


                                            } catch (
                                                error: Exception
                                            ) {

                                                reportErrorText =
                                                    error.message
                                                        ?: "Не удалось сформировать отчёт"


                                                reportScreenState =
                                                    ReportScreenState.MAIN
                                            }
                                        }
                                    }
                                },


                                // =====================================
                                // СКАЧИВАЕМ УЖЕ ГОТОВЫЙ ФАЙЛ
                                // =====================================

                                onDownloadClick = {

                                    val bytes =
                                        reportFileBytes


                                    if (bytes != null) {

                                        scope.launch {

                                            try {

                                                saveExcelReport(
                                                    context = context,
                                                    bytes = bytes
                                                )


                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Отчёт сохранён в «Загрузки»",
                                                        Toast.LENGTH_LONG
                                                    )
                                                    .show()


                                            } catch (
                                                error: Exception
                                            ) {

                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Не удалось сохранить отчёт",
                                                        Toast.LENGTH_LONG
                                                    )
                                                    .show()
                                            }
                                        }
                                    }
                                },


                                // =====================================
                                // "ЗАКРЫТЬ"
                                // =====================================

                                onCloseClick = {

                                    reportScreenState =
                                        ReportScreenState.MAIN

                                    reportFileBytes =
                                        null

                                    reportErrorText =
                                        null
                                },


                                // =====================================
                                // СТРЕЛКА НАЗАД НА ЗАГРУЗКЕ
                                // =====================================

                                onLoadingBackClick = {

                                    reportScreenState =
                                        ReportScreenState.MAIN
                                },


                                // =====================================
                                // НИЖНЯЯ НАВИГАЦИЯ
                                // =====================================

                                onChatsClick = {

                                    reportScreenState =
                                        ReportScreenState.MAIN

                                    currentScreen =
                                        AppScreen.CHATS
                                },


                                onEmployeesClick = {

                                    reportScreenState =
                                        ReportScreenState.MAIN

                                    currentScreen =
                                        AppScreen.EMPLOYEES
                                },


                                onNotificationsClick = {

                                    reportScreenState =
                                        ReportScreenState.MAIN

                                    currentScreen =
                                        AppScreen.NOTIFICATIONS
                                }
                            )


                        } else {

                            LaunchedEffect(Unit) {

                                currentScreen =
                                    AppScreen.CHATS
                            }
                        }
                    }
                }
            }
        }
    }
}


// =========================================
// MESSAGE → UI
// =========================================

private fun MessageResponse.toUi(
    employeeDeviceId: String
): ChatMessageUi {

    return ChatMessageUi(

        id =
            messageId,


        text =
            text.orEmpty(),


        time =
            formatMessageTime(
                createdAt
            ),


        createdAt =
            createdAt,


        isEmployee =
            senderDeviceId ==
                    employeeDeviceId,


        messageType =
            messageType,


        mediaMimeType =
            media?.mimeType,


        staticLatitude =
            staticLocation?.latitude,


        staticLongitude =
            staticLocation?.longitude,


        liveLocationEndsAt =
            liveLocation?.endsAt
    )
}


// =========================================
// СОХРАНЕНИЕ EXCEL
// =========================================

private suspend fun saveExcelReport(
    context: Context,
    bytes: ByteArray
) {

    withContext(
        Dispatchers.IO
    ) {

        val fileName =
            "gibdd_report_${
                LocalDateTime
                    .now()
                    .format(
                        DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd_HH-mm"
                        )
                    )
            }.xlsx"


        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.Q
        ) {

            val resolver =
                context.contentResolver


            val values =
                ContentValues().apply {

                    put(
                        MediaStore
                            .MediaColumns
                            .DISPLAY_NAME,
                        fileName
                    )


                    put(
                        MediaStore
                            .MediaColumns
                            .MIME_TYPE,

                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    )


                    put(
                        MediaStore
                            .MediaColumns
                            .RELATIVE_PATH,

                        Environment
                            .DIRECTORY_DOWNLOADS
                    )


                    put(
                        MediaStore
                            .MediaColumns
                            .IS_PENDING,
                        1
                    )
                }


            val uri =
                resolver.insert(
                    MediaStore
                        .Downloads
                        .EXTERNAL_CONTENT_URI,

                    values
                )
                    ?: error(
                        "Не удалось создать файл"
                    )


            resolver
                .openOutputStream(uri)
                ?.use { output ->

                    output.write(bytes)
                }
                ?: error(
                    "Не удалось открыть файл"
                )


            values.clear()


            values.put(
                MediaStore
                    .MediaColumns
                    .IS_PENDING,
                0
            )


            resolver.update(
                uri,
                values,
                null,
                null
            )


        } else {

            val directory =
                context.getExternalFilesDir(
                    Environment
                        .DIRECTORY_DOWNLOADS
                )
                    ?: context.filesDir


            val file =
                File(
                    directory,
                    fileName
                )


            file.outputStream()
                .use { output ->

                    output.write(bytes)
                }
        }
    }
}

private fun Throwable.userMessage(
    prefix: String
): String {

    val httpCode =
        (
                this as?
                        HttpException
                )
            ?.code()


    return if (
        httpCode != null
    ) {

        "$prefix (HTTP $httpCode)"

    } else {

        "$prefix: ${message.orEmpty()}"
    }
}


// =========================================
// ЭКРАН ОШИБКИ
// =========================================

@Composable
private fun ConnectionError(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                "Не удалось подключиться к серверу"
        )


        Text(
            text = message,

            modifier = Modifier.padding(
                vertical = 16.dp
            )
        )


        Button(
            onClick = onRetry
        ) {

            Text(
                text = "Повторить"
            )
        }
    }
}