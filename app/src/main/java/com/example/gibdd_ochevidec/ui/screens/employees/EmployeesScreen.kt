package com.example.gibdd_ochevidec.ui.screens.employees

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.gibdd_ochevidec.ui.components.AppBottomNavigation
import com.example.gibdd_ochevidec.ui.components.MainTab
import com.example.gibdd_ochevidec.ui.theme.Commissioner


data class EmployeeItem(
    val deviceId: String,
    val name: String?,
    val role: String,
    val lastActivity: String?
)


@Composable
fun EmployeesScreen(
    employees: List<EmployeeItem> = emptyList(),

    showNotificationsTab: Boolean = false,
    showReportsTab: Boolean = false,

    onChatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},

    onAddEmployeeClick: () -> Unit = {},
    onEmployeeClick: (EmployeeItem) -> Unit = {}
) {

    val backgroundColor = Color(0xFFF5F8FD)
    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF626776)
    val blue = Color(0xFF087CF0)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
    ) {

        // =========================================
        // ОСНОВНАЯ ЧАСТЬ ЭКРАНА
        // =========================================

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            Spacer(
                modifier = Modifier.height(34.dp)
            )


            // =========================================
            // ЗАГОЛОВОК + КНОПКА "+"
            // =========================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 28.dp,
                        end = 28.dp
                    ),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Сотрудники",
                        fontFamily = Commissioner,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 23.sp,
                        color = darkText
                    )


                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )


                    Text(
                        text =
                            "${employees.size} ${deviceWord(employees.size)}",

                        fontFamily = Commissioner,
                        fontSize = 13.sp,
                        color = grayText
                    )
                }


                // КНОПКА ДОБАВЛЕНИЯ
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            color = blue,
                            shape = CircleShape
                        )
                        .clickable {
                            onAddEmployeeClick()
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription =
                            "Добавить сотрудника",

                        tint = Color.White,

                        modifier = Modifier.size(19.dp)
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =========================================
            // СПИСОК СОТРУДНИКОВ
            // =========================================

            if (employees.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    contentAlignment = Alignment.TopCenter
                ) {

                    Text(
                        text = "Сотрудников пока нет",
                        fontFamily = Commissioner,
                        fontSize = 14.sp,
                        color = grayText,

                        modifier = Modifier.padding(
                            top = 30.dp
                        )
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    contentPadding = PaddingValues(
                        start = 28.dp,
                        end = 28.dp,
                        bottom = 20.dp
                    ),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = employees,

                        key = {
                            it.deviceId
                        }
                    ) { employee ->

                        EmployeeCard(
                            employee = employee,
                            darkText = darkText,
                            grayText = grayText,
                            blue = blue,

                            onClick = {
                                onEmployeeClick(employee)
                            }
                        )
                    }
                }
            }
        }


        // =========================================
        // ОБЩАЯ НИЖНЯЯ НАВИГАЦИЯ
        // =========================================

        AppBottomNavigation(
            selectedTab = MainTab.EMPLOYEES,

            showEmployees = true,

            showNotifications =
                showNotificationsTab,

            showReports =
                showReportsTab,

            onChatsClick =
                onChatsClick,

            onEmployeesClick = {
                // Уже на вкладке "Сотрудники"
            },

            onNotificationsClick =
                onNotificationsClick,

            onReportsClick =
                onReportsClick
        )
    }
}


@Composable
private fun EmployeeCard(
    employee: EmployeeItem,
    darkText: Color,
    grayText: Color,
    blue: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(17.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 18.dp,
                    end = 16.dp
                ),

            verticalAlignment = Alignment.CenterVertically
        ) {

            // АВАТАР
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color(0xFFF0F4F9),
                        shape = CircleShape
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFFBCC5D1),

                    modifier = Modifier.size(29.dp)
                )
            }


            Spacer(
                modifier = Modifier.width(16.dp)
            )


            // ИНФОРМАЦИЯ
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text =
                        employee.name
                            ?: "Устройство ${
                                employee.deviceId.take(8)
                            }",

                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = darkText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


                Spacer(
                    modifier = Modifier.height(3.dp)
                )


                Text(
                    text = roleTitle(employee.role),
                    fontFamily = Commissioner,
                    fontSize = 12.sp,
                    color = darkText
                )


                Spacer(
                    modifier = Modifier.height(3.dp)
                )


                Text(
                    text = "ID: ${employee.deviceId}",
                    fontFamily = Commissioner,
                    fontSize = 11.sp,
                    color = grayText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


            // ПОСЛЕДНЯЯ АКТИВНОСТЬ
            if (
                !employee.lastActivity
                    .isNullOrBlank()
            ) {

                Text(
                    text = employee.lastActivity,

                    fontFamily = Commissioner,
                    fontSize = 9.sp,
                    color = grayText,

                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(
                            top = 17.dp
                        )
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


private fun deviceWord(
    count: Int
): String {

    val lastTwo =
        count % 100

    val last =
        count % 10


    return when {

        lastTwo in 11..14 ->
            "устройств"

        last == 1 ->
            "устройство"

        last in 2..4 ->
            "устройства"

        else ->
            "устройств"
    }
}