package com.example.gibdd_ochevidec.ui.screens.employees

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gibdd_ochevidec.ui.theme.Commissioner


@Composable
fun EmployeeDeviceScreen(
    deviceId: String,
    role: String?,

    alias: String? = null,
    lastActivity: String? = null,

    onBackClick: () -> Unit = {},
    onRoleClick: () -> Unit = {},
    onDeleteRoleClick: () -> Unit = {}
) {

    val backgroundColor = Color(0xFFF5F8FD)
    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF8A8F9D)
    val blue = Color(0xFF087CF0)
    val red = Color(0xFFE31B23)
    val borderColor = Color(0xFFE4E8EF)

    val displayName =
        if (!alias.isNullOrBlank()) {
            alias
        } else {
            "Устройство ${deviceId.take(8)}"
        }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
    ) {

        Spacer(
            modifier = Modifier.height(30.dp)
        )


        // =================================================
        // ШАПКА
        // =================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 28.dp,
                    end = 28.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onBackClick()
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
                text = "Профиль устройства",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = darkText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


        Spacer(
            modifier = Modifier.height(30.dp)
        )


        // =================================================
        // АВАТАР + ИМЯ + РОЛЬ
        // =================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 28.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(78.dp)
                    .background(
                        color = Color(0xFFE7EBF0),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color(0xFFAFB8C5),
                    modifier = Modifier.size(48.dp)
                )
            }


            Spacer(
                modifier = Modifier.width(20.dp)
            )


            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = displayName,
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = darkText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                if (role != null) {

                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = blue,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 3.dp
                            )
                    ) {

                        Text(
                            text = roleTitle(role),
                            fontFamily = Commissioner,
                            fontSize = 12.sp,
                            color = blue
                        )
                    }

                } else {

                    Text(
                        text = "Роль не назначена",
                        fontFamily = Commissioner,
                        fontSize = 12.sp,
                        color = grayText
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        // =================================================
        // КАРТОЧКА С ДАННЫМИ
        // =================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 28.dp
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(14.dp)
                )
        ) {

            ProfileInfoRow(
                title = "ID устройства",
                value = deviceId,
                grayText = grayText,
                darkText = darkText
            )


            ProfileDivider(
                color = borderColor
            )


            ProfileInfoRow(
                title = "Локальный псевдоним",
                value =
                    if (!alias.isNullOrBlank()) {
                        alias
                    } else {
                        "Не задан"
                    },
                grayText = grayText,
                darkText = darkText
            )


            ProfileDivider(
                color = borderColor
            )


            ProfileInfoRow(
                title = "Роль",
                value =
                    role?.let {
                        roleTitle(it)
                    } ?: "Не назначена",
                grayText = grayText,
                darkText = darkText
            )


            ProfileDivider(
                color = borderColor
            )


            ProfileInfoRow(
                title = "Последняя активность",
                value =
                    if (!lastActivity.isNullOrBlank()) {
                        lastActivity
                    } else {
                        "Нет данных"
                    },
                grayText = grayText,
                darkText = darkText
            )
        }


        Spacer(
            modifier = Modifier.weight(1f)
        )


        // =================================================
        // ИЗМЕНИТЬ РОЛЬ
        // =================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 28.dp
                )
                .height(48.dp)
                .border(
                    width = 1.dp,
                    color = blue,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    onRoleClick()
                },
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Edit,
                    contentDescription = null,
                    tint = blue,
                    modifier = Modifier.size(18.dp)
                )


                Spacer(
                    modifier = Modifier.width(9.dp)
                )


                Text(
                    text =
                        if (role == null) {
                            "Назначить роль"
                        } else {
                            "Изменить роль"
                        },
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = blue
                )
            }
        }


        // =================================================
        // УДАЛИТЬ РОЛЬ
        // Показываем только если роль уже существует
        // =================================================

        if (role != null) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 28.dp
                    )
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = red,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        onDeleteRoleClick()
                    },
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = red,
                        modifier = Modifier.size(18.dp)
                    )


                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )


                    Text(
                        text = "Удалить роль",
                        fontFamily = Commissioner,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = red
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}


@Composable
private fun ProfileInfoRow(
    title: String,
    value: String,
    grayText: Color,
    darkText: Color
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
    ) {

        Text(
            text = title,
            fontFamily = Commissioner,
            fontSize = 12.sp,
            color = grayText
        )


        Spacer(
            modifier = Modifier.height(7.dp)
        )


        Text(
            text = value,
            fontFamily = Commissioner,
            fontSize = 13.sp,
            color = darkText,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
private fun ProfileDivider(
    color: Color
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color)
    )
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