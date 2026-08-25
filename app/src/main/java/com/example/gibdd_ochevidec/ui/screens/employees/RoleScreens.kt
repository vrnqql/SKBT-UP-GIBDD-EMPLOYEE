package com.example.gibdd_ochevidec.ui.screens.employees

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gibdd_ochevidec.ui.theme.Commissioner
import androidx.compose.material.icons.automirrored.filled.ArrowBack


// =====================================================
// 1. ВЫБОР РОЛИ
// =====================================================

@Composable
fun RoleSelectionScreen(
    deviceId: String,
    currentRole: String?,
    managerRole: String,

    errorText: String? = null,
    isLoading: Boolean = false,

    onBackClick: () -> Unit = {},
    onRoleSelected: (String) -> Unit = {}
) {

    val blue = Color(0xFF087CF0)
    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF626776)
    val borderColor = Color(0xFFE4E8EF)

    val roles =
        if (managerRole == "CHIEF") {

            listOf(
                RoleOption(
                    code = "INSPECTOR",
                    title = "Инспектор",
                    description =
                        "Доступ к чатам и ответам\nочевидцам"
                ),
                RoleOption(
                    code = "ADMIN",
                    title = "Администратор",
                    description =
                        "Доступ к чатам, сотрудникам\nи управлению ролями"
                ),
                RoleOption(
                    code = "CHIEF",
                    title = "Начальник",
                    description =
                        "Полный доступ к функциям\nприложения"
                )
            )

        } else {

            listOf(
                RoleOption(
                    code = "INSPECTOR",
                    title = "Инспектор",
                    description =
                        "Доступ к чатам и ответам\nочевидцам"
                ),
                RoleOption(
                    code = "ADMIN",
                    title = "Администратор",
                    description =
                        "Доступ к чатам, сотрудникам\nи управлению ролями"
                )
            )
        }


    var selectedRole by remember(
        currentRole
    ) {
        mutableStateOf(
            currentRole
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .padding(
                horizontal = 28.dp
            )
    ) {

        Spacer(
            modifier = Modifier.height(36.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
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

                androidx.compose.material3.Icon(
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
                text = "Выберите роль",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = darkText
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Text(
            text = "Устройство ID: $deviceId",
            fontFamily = Commissioner,
            fontSize = 12.sp,
            color = grayText
        )


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        roles.forEach { role ->

            RoleOptionCard(
                role = role,
                selected =
                    selectedRole == role.code,
                blue = blue,
                darkText = darkText,
                grayText = grayText,
                borderColor = borderColor,

                onClick = {

                    if (!isLoading) {
                        selectedRole = role.code
                    }
                }
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }


        if (!errorText.isNullOrBlank()) {

            Spacer(
                modifier = Modifier.height(6.dp)
            )


            Text(
                text = errorText,
                fontFamily = Commissioner,
                fontSize = 12.sp,
                color = Color(0xFFE31B23)
            )
        }


        Spacer(
            modifier = Modifier.weight(1f)
        )


        Button(
            onClick = {

                selectedRole?.let {
                    onRoleSelected(it)
                }
            },

            enabled =
                selectedRole != null &&
                        !isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape =
                RoundedCornerShape(12.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor = blue
                )
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )

            } else {

                Text(
                    text =
                        if (currentRole == null) {
                            "Назначить роль"
                        } else {
                            "Продолжить"
                        },

                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )
    }
}


private data class RoleOption(
    val code: String,
    val title: String,
    val description: String
)


@Composable
private fun RoleOptionCard(
    role: RoleOption,
    selected: Boolean,

    blue: Color,
    darkText: Color,
    grayText: Color,
    borderColor: Color,

    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color =
                    if (selected) {
                        blue
                    } else {
                        borderColor
                    },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 14.dp,
                vertical = 13.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    color = blue,
                    shape = CircleShape
                ),

            contentAlignment =
                Alignment.Center
        ) {

            androidx.compose.material3.Icon(
                imageVector =
                    Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }


        Spacer(
            modifier = Modifier.width(12.dp)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = role.title,
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = darkText
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(
                text = role.description,
                fontFamily = Commissioner,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                color = grayText
            )
        }


        Box(
            modifier = Modifier
                .size(20.dp)
                .border(
                    width = 1.dp,
                    color =
                        if (selected) {
                            blue
                        } else {
                            borderColor
                        },
                    shape = CircleShape
                ),
            contentAlignment =
                Alignment.Center
        ) {

            if (selected) {

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            blue,
                            CircleShape
                        )
                )
            }
        }
    }
}


// =====================================================
// 2. ПОДТВЕРЖДЕНИЕ ЗАМЕНЫ РОЛИ
// =====================================================

@Composable
fun ReplaceRoleConfirmationScreen(
    deviceId: String,
    oldRole: String,
    newRole: String,

    errorText: String? = null,
    isLoading: Boolean = false,

    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {

    val blue = Color(0xFF087CF0)
    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF626776)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .padding(
                horizontal = 28.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.weight(0.72f)
        )


        Text(
            text = "Замена роли",
            fontFamily = Commissioner,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = darkText
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        Text(
            text =
                "У устройства уже назначена\nроль «${roleTitle(oldRole)}»",

            fontFamily = Commissioner,
            fontSize = 13.sp,
            lineHeight = 17.sp,
            textAlign = TextAlign.Center,
            color = grayText
        )


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        Text(
            text =
                "Заменить на роль\n«${roleTitle(newRole)}»?",

            fontFamily = Commissioner,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            color = darkText
        )


        if (!errorText.isNullOrBlank()) {

            Spacer(
                modifier = Modifier.height(18.dp)
            )


            Text(
                text = errorText,
                fontFamily = Commissioner,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFFE31B23)
            )
        }


        Spacer(
            modifier = Modifier.weight(1f)
        )


        Button(
            onClick = onConfirm,
            enabled = !isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape =
                RoundedCornerShape(12.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor = blue
                )
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )

            } else {

                Text(
                    text = "Заменить",
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        OutlinedButton(
            onClick = onCancel,
            enabled = !isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            border =
                BorderStroke(
                    width = 1.dp,
                    color = blue
                ),

            shape =
                RoundedCornerShape(12.dp)
        ) {

            Text(
                text = "Отмена",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = blue
            )
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )
    }
}


// =====================================================
// 3. РОЛЬ НАЗНАЧЕНА
// =====================================================

@Composable
fun RoleSuccessScreen(
    deviceId: String,
    role: String,
    onDoneClick: () -> Unit = {}
) {

    val darkText = Color(0xFF090B22)
    val blue = Color(0xFF087CF0)
    val green = Color(0xFF05B85C)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .padding(
                horizontal = 28.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.weight(0.8f)
        )


        Box(
            modifier = Modifier
                .size(90.dp)
                .background(
                    color = Color(0xFFD8F5E4),
                    shape = CircleShape
                ),

            contentAlignment =
                Alignment.Center
        ) {

            androidx.compose.material3.Icon(
                imageVector =
                    Icons.Default.Check,
                contentDescription = null,
                tint = green,
                modifier = Modifier.size(48.dp)
            )
        }


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        Text(
            text = "Роль назначена",
            fontFamily = Commissioner,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = darkText
        )


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        Text(
            text =
                "Устройство ID: $deviceId\nназначена роль\n«${roleTitle(role)}»",

            fontFamily = Commissioner,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF626776)
        )


        Spacer(
            modifier = Modifier.weight(1f)
        )


        Button(
            onClick = onDoneClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape =
                RoundedCornerShape(12.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor = blue
                )
        ) {

            Text(
                text = "Готово",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )
    }
}


// =====================================================
// 4. ОШИБКА НАЗНАЧЕНИЯ РОЛИ
// =====================================================

@Composable
fun RoleErrorScreen(
    reason: String =
        "Устройство не зарегистрировано",

    onRetryScanClick: () -> Unit = {},
    onCloseClick: () -> Unit = {}
) {

    val darkText = Color(0xFF090B22)
    val blue = Color(0xFF087CF0)
    val red = Color(0xFFE31B23)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .padding(
                horizontal = 28.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.weight(0.8f)
        )


        Box(
            modifier = Modifier
                .size(90.dp)
                .background(
                    color = Color(0xFFFFE1E1),
                    shape = CircleShape
                ),

            contentAlignment =
                Alignment.Center
        ) {

            androidx.compose.material3.Icon(
                imageVector =
                    Icons.Default.Close,
                contentDescription = null,
                tint = red,
                modifier = Modifier.size(48.dp)
            )
        }


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        Text(
            text = "Не удалось выдать роль",
            fontFamily = Commissioner,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = darkText,
            textAlign = TextAlign.Center
        )


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        Text(
            text =
                "Роль не может быть выдана,\nпо причине: $reason",

            fontFamily = Commissioner,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF626776)
        )


        Spacer(
            modifier = Modifier.weight(1f)
        )


        Button(
            onClick = onRetryScanClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape =
                RoundedCornerShape(12.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor = blue
                )
        ) {

            Text(
                text = "Повторить сканирование",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        OutlinedButton(
            onClick = onCloseClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            border =
                BorderStroke(
                    width = 1.dp,
                    color = blue
                ),

            shape =
                RoundedCornerShape(12.dp)
        ) {

            Text(
                text = "Закрыть",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = blue
            )
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )
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