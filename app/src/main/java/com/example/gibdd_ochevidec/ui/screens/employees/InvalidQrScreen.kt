package com.example.gibdd_ochevidec.ui.screens.employees

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gibdd_ochevidec.ui.theme.Commissioner

@Composable
fun InvalidQrScreen(
    onRetryClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF626776)
    val blue = Color(0xFF087CF0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FD))
            .safeDrawingPadding()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "QR-код недействителен",
            fontFamily = Commissioner,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp,
            color = darkText,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "QR-код не содержит корректный ID устройства сотрудника.",
            fontFamily = Commissioner,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = grayText,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            onClick = onRetryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = blue
            )
        ) {

            Text(
                text = "Сканировать ещё раз",
                fontFamily = Commissioner,
                fontSize = 15.sp,
                color = Color.White
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE5EEF9)
            )
        ) {

            Text(
                text = "Назад",
                fontFamily = Commissioner,
                fontSize = 15.sp,
                color = darkText
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}