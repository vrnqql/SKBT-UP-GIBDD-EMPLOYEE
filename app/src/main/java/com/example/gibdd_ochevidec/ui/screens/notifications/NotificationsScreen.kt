package com.example.gibdd_ochevidec.ui.screens.notifications

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gibdd_ochevidec.ui.components.AppBottomNavigation
import com.example.gibdd_ochevidec.ui.components.MainTab
import com.example.gibdd_ochevidec.ui.theme.Commissioner


data class NotificationItem(
    val id: String,
    val title: String,
    val text: String,
    val time: String
)


@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem> = emptyList(),

    onChatsClick: () -> Unit = {},
    onEmployeesClick: () -> Unit = {},
    onReportsClick: () -> Unit = {}
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

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            Spacer(
                modifier = Modifier.height(36.dp)
            )


            Text(
                text = "Уведомления",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 23.sp,
                color = darkText,

                modifier = Modifier.padding(
                    horizontal = 28.dp
                )
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            if (notifications.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    contentAlignment = Alignment.TopCenter
                ) {

                    Text(
                        text = "Новых уведомлений пока нет",
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
                        Arrangement.spacedBy(10.dp)
                ) {

                    items(
                        items = notifications,
                        key = {
                            it.id
                        }
                    ) { notification ->

                        NotificationCard(
                            notification = notification,
                            darkText = darkText,
                            grayText = grayText,
                            blue = blue
                        )
                    }
                }
            }
        }


        AppBottomNavigation(
            selectedTab = MainTab.NOTIFICATIONS,

            showEmployees = true,
            showNotifications = true,
            showReports = true,

            onChatsClick = onChatsClick,
            onEmployeesClick = onEmployeesClick,

            onNotificationsClick = {
                // Уже на вкладке "Уведомления"
            },

            onReportsClick = onReportsClick
        )
    }
}


@Composable
private fun NotificationCard(
    notification: NotificationItem,
    darkText: Color,
    grayText: Color,
    blue: Color
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp),

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
                    start = 16.dp,
                    end = 16.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = Color(0xFFF0F6FE),
                        shape = CircleShape
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Notifications,

                    contentDescription = null,

                    tint = blue,

                    modifier =
                        Modifier.size(25.dp)
                )
            }


            Spacer(
                modifier = Modifier.width(15.dp)
            )


            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = notification.title,
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = darkText
                )


                Spacer(
                    modifier = Modifier.height(4.dp)
                )


                Text(
                    text = notification.text,
                    fontFamily = Commissioner,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    color = darkText
                )
            }


            Spacer(
                modifier = Modifier.width(6.dp)
            )


            Text(
                text = notification.time,
                fontFamily = Commissioner,
                fontSize = 9.sp,
                color = grayText,

                modifier =
                    Modifier.align(
                        Alignment.Top
                    )
                        .padding(
                            top = 16.dp
                        )
            )
        }
    }
}