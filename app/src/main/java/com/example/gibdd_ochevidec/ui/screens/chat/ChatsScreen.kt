package com.example.gibdd_ochevidec.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
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


data class ChatItem(
    val id: String,
    val name: String,
    val message: String,
    val time: String,
    val unreadCount: Int = 0,
    val isCar: Boolean = false,
    val isBanned: Boolean = false
)


@Composable
fun ChatsScreen(
    chats: List<ChatItem> = emptyList(),
    aliases: Map<String, String> = emptyMap(),

    showEmployeesTab: Boolean = false,
    showNotificationsTab: Boolean = false,
    showReportsTab: Boolean = false,

    onChatClick: (String, String) -> Unit = { _, _ -> },

    onEmployeesClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onReportsClick: () -> Unit = {}
) {

    val backgroundColor = Color(0xFFF5F8FD)
    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF555B6B)
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
                text = "Чаты",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 23.sp,
                color = darkText,
                modifier = Modifier.padding(
                    horizontal = 28.dp
                )
            )


            Spacer(
                modifier = Modifier.height(30.dp)
            )


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

                if (chats.isEmpty()) {

                    item {

                        Text(
                            text = "Новых обращений пока нет",
                            fontFamily = Commissioner,
                            fontSize = 14.sp,
                            color = grayText
                        )
                    }

                } else {

                    itemsIndexed(
                        items = chats,
                        key = { _, chat ->
                            chat.id
                        }
                    ) { _, chat ->

                        val displayChat =
                            chat.copy(
                                name =
                                    aliases[chat.id]
                                        ?: chat.name
                            )


                        ChatCard(
                            chat = displayChat,
                            darkText = darkText,
                            grayText = grayText,
                            blue = blue,

                            onClick = {

                                onChatClick(
                                    displayChat.id,
                                    displayChat.name
                                )
                            }
                        )
                    }
                }
            }
        }


        AppBottomNavigation(
            selectedTab = MainTab.CHATS,

            showEmployees =
                showEmployeesTab,

            showNotifications =
                showNotificationsTab,

            showReports =
                showReportsTab,

            onChatsClick = {
                // Уже на вкладке "Чаты"
            },

            onEmployeesClick =
                onEmployeesClick,

            onNotificationsClick =
                onNotificationsClick,

            onReportsClick =
                onReportsClick
        )
    }
}


@Composable
private fun ChatCard(
    chat: ChatItem,
    darkText: Color,
    grayText: Color,
    blue: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                if (chat.isBanned) {
                    100.dp
                } else {
                    88.dp
                }
            )
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 17.dp,
                    end = 16.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            if (chat.isCar) {

                CarPlaceholder()

            } else {

                PersonAvatar(
                    blue = blue
                )
            }


            Spacer(
                modifier = Modifier.width(15.dp)
            )


            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = chat.name,
                        fontFamily = Commissioner,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = darkText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,

                        modifier = Modifier.weight(
                            weight = 1f,
                            fill = false
                        )
                    )


                    if (chat.isBanned) {

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )


                        Icon(
                            imageVector =
                                Icons.Default.Block,

                            contentDescription =
                                "Заблокирован",

                            tint =
                                Color(0xFFD34A4A),

                            modifier =
                                Modifier.size(15.dp)
                        )
                    }
                }


                if (chat.isBanned) {

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )


                    Text(
                        text = "Пользователь заблокирован",
                        fontFamily = Commissioner,
                        fontSize = 10.sp,
                        color = Color(0xFFD34A4A),
                        maxLines = 1
                    )
                }


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                Text(
                    text = chat.message,
                    fontFamily = Commissioner,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = grayText,

                    maxLines =
                        if (chat.isBanned) {
                            1
                        } else {
                            2
                        },

                    overflow =
                        TextOverflow.Ellipsis
                )
            }


            Spacer(
                modifier = Modifier.width(8.dp)
            )


            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        top = 15.dp,
                        bottom = 14.dp
                    ),

                horizontalAlignment =
                    Alignment.End
            ) {

                Text(
                    text = chat.time,
                    fontFamily = Commissioner,
                    fontSize = 9.sp,
                    color = grayText,
                    maxLines = 1
                )


                Spacer(
                    modifier = Modifier.weight(1f)
                )


                if (chat.unreadCount > 0) {

                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .background(
                                color = blue,
                                shape = CircleShape
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text =
                                chat.unreadCount
                                    .toString(),

                            fontFamily = Commissioner,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun PersonAvatar(
    blue: Color
) {

    Box(
        modifier = Modifier
            .size(52.dp)
            .background(
                color = Color(0xFFF1F7FF),
                shape = CircleShape
            ),

        contentAlignment =
            Alignment.Center
    ) {

        Icon(
            imageVector =
                Icons.Default.Person,

            contentDescription =
                "Очевидец",

            tint = blue,

            modifier =
                Modifier.size(30.dp)
        )
    }
}


@Composable
private fun CarPlaceholder() {

    Box(
        modifier = Modifier
            .size(52.dp)
            .background(
                color = Color(0xFFE8EDF3),
                shape = CircleShape
            ),

        contentAlignment =
            Alignment.Center
    ) {

        Text(
            text = "🚗",
            fontSize = 25.sp
        )
    }
}