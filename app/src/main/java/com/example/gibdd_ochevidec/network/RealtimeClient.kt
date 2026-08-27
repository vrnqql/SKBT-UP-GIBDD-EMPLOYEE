package com.example.gibdd_ochevidec.network

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit


class RealtimeClient {

    private val client =
        OkHttpClient.Builder()
            .pingInterval(
                20,
                TimeUnit.SECONDS
            )
            .build()


    private val handler =
        Handler(
            Looper.getMainLooper()
        )


    private var webSocket: WebSocket? =
        null


    private var accessToken: String? =
        null


    private var messageListener:
            ((String) -> Unit)? =
        null


    private var shouldReconnect =
        false


    private var reconnectScheduled =
        false


    fun connect(
        accessToken: String,
        onMessage: (String) -> Unit
    ) {

        messageListener =
            onMessage

        shouldReconnect =
            true


        // Если токен тот же и соединение уже есть —
        // ничего заново не создаём.
        if (
            this.accessToken == accessToken &&
            webSocket != null
        ) {
            return
        }


        // Если backend выдал новый access_token,
        // старое соединение больше использовать нельзя.
        if (
            this.accessToken != null &&
            this.accessToken != accessToken
        ) {

            Log.d(
                "REALTIME",
                "Access token changed, reconnecting WebSocket"
            )


            webSocket?.cancel()

            webSocket =
                null
        }


        this.accessToken =
            accessToken


        handler.removeCallbacksAndMessages(
            null
        )

        reconnectScheduled =
            false


        openSocket()
    }


    private fun openSocket() {

        val token =
            accessToken
                ?: return


        val request =
            Request.Builder()
                .url(
                    "wss://xn--e1afhclgq.xn--p1ai:4401/api/v1/realtime"
                )
                .addHeader(
                    "Authorization",
                    "Bearer $token"
                )
                .addHeader(
                    "X-Client-App",
                    "employee"
                )
                .build()


        val socket =
            client.newWebSocket(
                request,

                object :
                    WebSocketListener() {


                    override fun onOpen(
                        webSocket: WebSocket,
                        response: Response
                    ) {

                        if (
                            this@RealtimeClient.webSocket !==
                            webSocket
                        ) {
                            return
                        }


                        Log.d(
                            "REALTIME",
                            "WebSocket connected"
                        )
                    }


                    override fun onMessage(
                        webSocket: WebSocket,
                        text: String
                    ) {

                        if (
                            this@RealtimeClient.webSocket !==
                            webSocket
                        ) {
                            return
                        }


                        Log.d(
                            "REALTIME",
                            "Event: $text"
                        )


                        messageListener
                            ?.invoke(text)
                    }


                    override fun onClosing(
                        webSocket: WebSocket,
                        code: Int,
                        reason: String
                    ) {

                        if (
                            this@RealtimeClient.webSocket !==
                            webSocket
                        ) {
                            return
                        }


                        Log.d(
                            "REALTIME",
                            "Closing: $code $reason"
                        )


                        webSocket.close(
                            code,
                            reason
                        )
                    }


                    override fun onClosed(
                        webSocket: WebSocket,
                        code: Int,
                        reason: String
                    ) {

                        if (
                            this@RealtimeClient.webSocket !==
                            webSocket
                        ) {
                            return
                        }


                        Log.d(
                            "REALTIME",
                            "Closed: $code $reason"
                        )


                        this@RealtimeClient
                            .webSocket =
                            null


                        scheduleReconnect()
                    }


                    override fun onFailure(
                        webSocket: WebSocket,
                        t: Throwable,
                        response: Response?
                    ) {

                        // Ошибка от уже заменённого старого
                        // WebSocket нас больше не интересует.
                        if (
                            this@RealtimeClient.webSocket !==
                            webSocket
                        ) {
                            return
                        }


                        Log.e(
                            "REALTIME",
                            "WebSocket error: ${t.message}",
                            t
                        )


                        this@RealtimeClient
                            .webSocket =
                            null


                        scheduleReconnect()
                    }
                }
            )


        webSocket =
            socket
    }


    private fun scheduleReconnect() {

        if (
            !shouldReconnect ||
            reconnectScheduled ||
            webSocket != null
        ) {
            return
        }


        reconnectScheduled =
            true


        Log.d(
            "REALTIME",
            "Reconnect in 5 seconds"
        )


        handler.postDelayed(
            {

                reconnectScheduled =
                    false


                if (
                    shouldReconnect &&
                    webSocket == null
                ) {

                    Log.d(
                        "REALTIME",
                        "Reconnecting..."
                    )


                    openSocket()
                }

            },
            5_000
        )
    }


    fun disconnect() {

        shouldReconnect =
            false

        reconnectScheduled =
            false


        handler.removeCallbacksAndMessages(
            null
        )


        val socket =
            webSocket


        webSocket =
            null


        socket?.close(
            1000,
            "App disconnect"
        )


        accessToken =
            null

        messageListener =
            null
    }
}