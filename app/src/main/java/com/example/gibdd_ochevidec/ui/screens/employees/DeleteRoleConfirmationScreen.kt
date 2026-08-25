package com.example.gibdd_ochevidec.ui.screens.employees

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
fun DeleteRoleConfirmationScreen(
    role: String,
    isLoading: Boolean = false,
    errorText: String? = null,
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {

    val darkText = Color(0xFF090B22)
    val blue = Color(0xFF087CF0)
    val red = Color(0xFFE31B23)
    val background = Color(0xFF969696)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .safeDrawingPadding()
    ) {

        // =========================================
        // ШАПКА
        // =========================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 28.dp,
                    end = 28.dp,
                    top = 30.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (!isLoading) {
                            onBackClick()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = darkText,
                    modifier = Modifier.size(22.dp)
                )
            }


            Spacer(
                modifier = Modifier.width(18.dp)
            )


            Text(
                text = "Удалить роль",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = darkText
            )
        }


        // =========================================
        // БЕЛАЯ КАРТОЧКА
        // =========================================

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 40.dp,
                    bottom = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Красный круг
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(
                        color = Color(0xFFFFE4E4),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = red,
                    modifier = Modifier.size(48.dp)
                )
            }


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            Text(
                text = "Удалить роль\n«${roleTitle(role)}»?",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 19.sp,
                lineHeight = 23.sp,
                textAlign = TextAlign.Center,
                color = darkText
            )


            Spacer(
                modifier = Modifier.height(13.dp)
            )


            Text(
                text = "Устройство потеряет доступ\nк мессенджеру.",
                fontFamily = Commissioner,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF5F6372)
            )


            if (!errorText.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(14.dp)
                )


                Text(
                    text = errorText,
                    fontFamily = Commissioner,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = red
                )
            }


            Spacer(
                modifier = Modifier.height(34.dp)
            )


            // =========================================
            // УДАЛИТЬ РОЛЬ
            // =========================================

            Button(
                onClick = onConfirmClick,
                enabled = !isLoading,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                shape = RoundedCornerShape(12.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = red,
                    disabledContainerColor =
                        Color(0xFFF19A9E)
                )
            ) {

                Text(
                    text =
                        if (isLoading) {
                            "Удаление..."
                        } else {
                            "Удалить роль"
                        },
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // =========================================
            // ОТМЕНА
            // =========================================

            OutlinedButton(
                onClick = onCancelClick,
                enabled = !isLoading,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                shape = RoundedCornerShape(12.dp),

                border = BorderStroke(
                    width = 1.dp,
                    color = blue
                ),

                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = blue
                    )
            ) {

                Text(
                    text = "Отмена",
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}


private fun roleTitle(
    role: String
): String {

    return when (role) {

        "INSPECTOR" ->
            "Инспектор"

        "ADMIN" ->
            "Администратор"

        "CHIEF" ->
            "Начальник"

        else ->
            role
    }
}