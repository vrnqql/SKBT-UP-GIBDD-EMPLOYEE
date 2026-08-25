package com.example.gibdd_ochevidec.ui.screens.common

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gibdd_ochevidec.ui.theme.Commissioner
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter


@Composable
fun WaitingRoleScreen(
    deviceId: String
) {

    val backgroundColor =
        Color(0xFFF5F8FD)

    val darkText =
        Color(0xFF090B22)

    val blue =
        Color(0xFF087CF0)

    val lightBlue =
        Color(0xFFDCEEFF)

    val logoPlaceholder =
        Color(0xFFDDEEFF)

    val grayText =
        Color(0xFF6B7080)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(40.dp)
            )


            // Заглушка вместо логотипа
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        logoPlaceholder
                    )
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
                            color =
                                Color(0xFF102AD3),

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
                text = "Получение доступа",

                fontFamily = Commissioner,

                fontSize = 15.sp,

                color = darkText
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(378.dp),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 22.dp,
                            end = 22.dp,
                            top = 28.dp,
                            bottom = 24.dp
                        ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {


                    QrCode(
                        content = deviceId,

                        modifier =
                            Modifier.size(205.dp)
                    )


                    Spacer(
                        modifier =
                            Modifier.height(9.dp)
                    )


                    Box(
                        modifier = Modifier
                            .width(58.dp)
                            .height(1.dp)
                            .background(
                                Color(0xFF9A9EA8)
                            )
                    )


                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )


                    Text(
                        text = "ID устройства",

                        fontFamily = Commissioner,

                        fontSize = 12.sp,

                        color = grayText
                    )


                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )


                    Text(
                        text =
                            if (deviceId.isNotBlank()) {

                                deviceId
                                    .substringBefore("-")
                                    .uppercase()

                            } else {

                                "--------"
                            },

                        fontFamily = Commissioner,

                        fontWeight =
                            FontWeight.SemiBold,

                        fontSize = 22.sp,

                        color = blue
                    )


                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )


                    Text(
                        text =
                            "Покажите QR-код администратору\n" +
                                    "или начальнику для назначения роли",

                        fontFamily = Commissioner,

                        fontSize = 12.sp,

                        color = grayText,

                        textAlign =
                            TextAlign.Center,

                        lineHeight = 16.sp
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(40.dp)
            )


            // =================================================
            // БОЛЬШЕ НЕ КНОПКА
            // Просто индикатор ожидания.
            // =================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        color = lightBlue,

                        shape =
                            RoundedCornerShape(
                                15.dp
                            )
                    ),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.Center
            ) {


                ClockIcon(
                    color = blue,

                    modifier =
                        Modifier.size(21.dp)
                )


                Spacer(
                    modifier =
                        Modifier.width(13.dp)
                )


                Text(
                    text =
                        "Ожидание назначения роли",

                    fontFamily = Commissioner,

                    fontWeight =
                        FontWeight.SemiBold,

                    fontSize = 14.sp,

                    color = blue
                )
            }
        }
    }
}


@Composable
private fun QrCode(
    content: String,
    modifier: Modifier = Modifier
) {

    val qrContent =
        if (content.isBlank()) {
            "waiting"
        } else {
            content
        }


    val qrBitmap =
        remember(qrContent) {

            val matrix =
                MultiFormatWriter()
                    .encode(
                        qrContent,
                        BarcodeFormat.QR_CODE,
                        600,
                        600
                    )


            Bitmap.createBitmap(
                matrix.width,
                matrix.height,
                Bitmap.Config.ARGB_8888
            ).apply {

                for (
                x in 0 until matrix.width
                ) {

                    for (
                    y in 0 until matrix.height
                    ) {

                        setPixel(
                            x,
                            y,

                            if (matrix[x, y]) {

                                Color.Black.toArgb()

                            } else {

                                Color.White.toArgb()
                            }
                        )
                    }
                }
            }.asImageBitmap()
        }


    Image(
        bitmap = qrBitmap,

        contentDescription =
            "QR-код устройства",

        modifier = modifier
    )
}


@Composable
private fun ClockIcon(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier = modifier
    ) {

        val strokeWidth =
            2.dp.toPx()


        drawCircle(
            color = color,

            style =
                Stroke(
                    width = strokeWidth
                )
        )


        drawLine(
            color = color,

            start =
                Offset(
                    x = size.width / 2,
                    y = size.height / 2
                ),

            end =
                Offset(
                    x = size.width / 2,
                    y = size.height * 0.25f
                ),

            strokeWidth =
                strokeWidth,

            cap =
                StrokeCap.Round
        )


        drawLine(
            color = color,

            start =
                Offset(
                    x = size.width / 2,
                    y = size.height / 2
                ),

            end =
                Offset(
                    x = size.width * 0.68f,
                    y = size.height * 0.62f
                ),

            strokeWidth =
                strokeWidth,

            cap =
                StrokeCap.Round
        )
    }
}