package com.example.gibdd_ochevidec.ui.screens.common

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gibdd_ochevidec.network.DeviceFingerprint
import com.example.gibdd_ochevidec.network.RegisterDeviceRequest
import com.example.gibdd_ochevidec.network.RetrofitClient
import com.example.gibdd_ochevidec.ui.theme.Commissioner


@Composable
fun LoadingScreen(
    onRegistered: (
        deviceId: String,
        role: String?
    ) -> Unit = { _, _ -> }
) {

    val context = LocalContext.current

    val backgroundColor = Color(0xFFF5F8FD)
    val darkText = Color(0xFF090B22)
    val blue = Color(0xFF071FD1)
    val lightBlue = Color(0xFFDDEEFF)
    val grayText = Color(0xFF616575)
    val errorColor = Color(0xFFD32F2F)

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var retryKey by remember {
        mutableStateOf(0)
    }


    // =====================================================
    // НАСТОЯЩАЯ РЕГИСТРАЦИЯ ЧЕРЕЗ API
    // =====================================================

    LaunchedEffect(retryKey) {

        errorMessage = null

        try {

            val fingerprintHash =
                DeviceFingerprint.getHash(context)


            Log.d(
                "EMPLOYEE_API",
                "Начинаем регистрацию устройства"
            )

            Log.d(
                "EMPLOYEE_API",
                "fingerprint_hash = $fingerprintHash"
            )


            val response =
                RetrofitClient.apiService.registerDevice(
                    request = RegisterDeviceRequest(
                        fingerprintHash = fingerprintHash,
                        pushToken = null
                    )
                )


            Log.d(
                "EMPLOYEE_API",
                "Регистрация успешна"
            )

            Log.d(
                "EMPLOYEE_API",
                "device_id = ${response.deviceId}"
            )

            Log.d(
                "EMPLOYEE_API",
                "role = ${response.role}"
            )


            // =================================================
            // СОХРАНЯЕМ DEVICE ID И ACCESS TOKEN
            // =================================================

            val sessionPreferences =
                context.getSharedPreferences(
                    "employee_session",
                    android.content.Context.MODE_PRIVATE
                )


            sessionPreferences
                .edit()
                .putString(
                    "device_id",
                    response.deviceId
                )
                .putString(
                    "access_token",
                    response.accessToken
                )
                .putString(
                    "role",
                    response.role
                )
                .apply()


            // Передаём результат в MainActivity
            onRegistered(
                response.deviceId,
                response.role
            )

        } catch (e: Exception) {

            Log.e(
                "EMPLOYEE_API",
                "Ошибка регистрации устройства",
                e
            )


            errorMessage =
                e.message
                    ?: "Не удалось зарегистрировать устройство"
        }
    }


    // =====================================================
    // ИНТЕРФЕЙС
    // =====================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(45.dp)
            )


            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(lightBlue)
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            Text(
                text = buildAnnotatedString {

                    withStyle(
                        SpanStyle(
                            color = darkText,
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    ) {
                        append("ГИБДД-")
                    }

                    withStyle(
                        SpanStyle(
                            color = blue,
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    ) {
                        append("Очевидец")
                    }
                },

                fontFamily = Commissioner,
                fontSize = 26.sp
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            Text(
                text = "Подготовка приложения",
                fontFamily = Commissioner,
                fontSize = 15.sp,
                color = darkText
            )


            Spacer(
                modifier = Modifier.height(70.dp)
            )


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),

                shape = RoundedCornerShape(18.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 42.dp
                        ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Spacer(
                        modifier = Modifier.height(25.dp)
                    )


                    if (errorMessage == null) {

                        LoadingCircle()

                    } else {

                        Text(
                            text = "!",
                            fontFamily = Commissioner,
                            fontWeight =
                                FontWeight.SemiBold,
                            fontSize = 46.sp,
                            color = errorColor
                        )
                    }


                    Spacer(
                        modifier = Modifier.weight(1f)
                    )


                    if (errorMessage == null) {

                        Text(
                            text =
                                "Регистрируем устройство",

                            fontFamily = Commissioner,

                            fontSize = 15.sp,

                            fontWeight =
                                FontWeight.SemiBold,

                            color = darkText
                        )


                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )


                        Text(
                            text =
                                "Это займёт несколько секунд",

                            fontFamily = Commissioner,

                            fontSize = 13.sp,

                            color = grayText
                        )

                    } else {

                        Text(
                            text =
                                "Не удалось зарегистрировать устройство",

                            fontFamily = Commissioner,

                            fontSize = 14.sp,

                            fontWeight =
                                FontWeight.SemiBold,

                            color = darkText
                        )


                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )


                        Text(
                            text = errorMessage ?: "",

                            fontFamily = Commissioner,

                            fontSize = 11.sp,

                            color = errorColor
                        )


                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )


                        Button(
                            onClick = {
                                retryKey++
                            },

                            shape =
                                RoundedCornerShape(10.dp),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = blue
                                )
                        ) {

                            Text(
                                text = "Повторить",

                                fontFamily =
                                    Commissioner,

                                color = Color.White
                            )
                        }
                    }
                }
            }


            Spacer(
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun LoadingCircle() {

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "loadingRotation"
        )


    val rotation =
        infiniteTransition.animateFloat(

            initialValue = 0f,

            targetValue = 360f,

            animationSpec =
                infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        easing = LinearEasing
                    )
                ),

            label = "loadingRotation"
        )


    Canvas(
        modifier = Modifier
            .size(105.dp)
            .rotate(rotation.value)
    ) {

        val strokeWidth =
            6.dp.toPx()


        drawArc(

            brush =
                Brush.linearGradient(
                    listOf(
                        Color(0xFF071FD1),
                        Color(0xFF1484DF),
                        Color(0xFF5595D6)
                    ),

                    start =
                        Offset(
                            size.width,
                            0f
                        ),

                    end =
                        Offset(
                            0f,
                            size.height
                        )
                ),

            startAngle = 20f,

            sweepAngle = 285f,

            useCenter = false,

            topLeft =
                Offset(
                    strokeWidth / 2,
                    strokeWidth / 2
                ),

            size =
                Size(
                    size.width - strokeWidth,
                    size.height - strokeWidth
                ),

            style =
                Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Butt
                )
        )
    }
}