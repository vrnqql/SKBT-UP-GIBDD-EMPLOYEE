package com.example.gibdd_ochevidec.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gibdd_ochevidec.ui.theme.Commissioner


enum class MainTab {
    CHATS,
    EMPLOYEES,
    NOTIFICATIONS,
    REPORTS
}


@Composable
fun AppBottomNavigation(
    selectedTab: MainTab,

    showEmployees: Boolean,
    showNotifications: Boolean,
    showReports: Boolean,

    onChatsClick: () -> Unit,
    onEmployeesClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onReportsClick: () -> Unit
) {

    val blue = Color(0xFF087CF0)
    val darkText = Color(0xFF090B22)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.White),

        horizontalArrangement =
            Arrangement.SpaceEvenly,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        BottomNavigationItem(
            title = "Чаты",
            selected =
                selectedTab == MainTab.CHATS,
            blue = blue,
            darkText = darkText,
            onClick = onChatsClick,
            icon = {
                    color ->

                Icon(
                    imageVector =
                        Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Чаты",
                    tint = color,
                    modifier = Modifier.size(23.dp)
                )
            }
        )


        if (showEmployees) {

            BottomNavigationItem(
                title = "Сотрудники",
                selected =
                    selectedTab == MainTab.EMPLOYEES,
                blue = blue,
                darkText = darkText,
                onClick = onEmployeesClick,
                icon = {
                        color ->

                    Icon(
                        imageVector =
                            Icons.Default.Person,
                        contentDescription =
                            "Сотрудники",
                        tint = color,
                        modifier = Modifier.size(23.dp)
                    )
                }
            )
        }


        if (showNotifications) {

            BottomNavigationItem(
                title = "Уведомления",
                selected =
                    selectedTab ==
                            MainTab.NOTIFICATIONS,
                blue = blue,
                darkText = darkText,
                onClick = onNotificationsClick,
                icon = {
                        color ->

                    Icon(
                        imageVector =
                            Icons.Outlined.Notifications,
                        contentDescription =
                            "Уведомления",
                        tint = color,
                        modifier = Modifier.size(23.dp)
                    )
                }
            )
        }


        if (showReports) {

            BottomNavigationItem(
                title = "Отчёты",
                selected =
                    selectedTab == MainTab.REPORTS,
                blue = blue,
                darkText = darkText,
                onClick = onReportsClick,
                icon = {
                        color ->

                    Icon(
                        imageVector =
                            Icons.Outlined.Assessment,
                        contentDescription =
                            "Отчёты",
                        tint = color,
                        modifier = Modifier.size(23.dp)
                    )
                }
            )
        }
    }
}


@Composable
private fun BottomNavigationItem(
    title: String,
    selected: Boolean,
    blue: Color,
    darkText: Color,
    onClick: () -> Unit,
    icon: @Composable (Color) -> Unit
) {

    val color =
        if (selected) {
            blue
        } else {
            darkText
        }


    Column(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 6.dp,
                vertical = 7.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        icon(color)


        Spacer(
            modifier = Modifier.height(3.dp)
        )


        Text(
            text = title,
            fontFamily = Commissioner,
            fontWeight =
                if (selected) {
                    FontWeight.Medium
                } else {
                    FontWeight.Normal
                },
            fontSize = 9.sp,
            color = color
        )
    }
}