package com.example.gibdd_ochevidec.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
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
import com.example.gibdd_ochevidec.ui.theme.Commissioner


data class ChatItem(
    val id: String,
    val name: String,
    val message: String,
    val time: String,
    val unreadCount: Int = 0,
    val isCar: Boolean = false
)


@Composable
fun ChatsScreen(
    hiddenChatIds: Set<String> = emptySet(),
    aliases: Map<String, String> = emptyMap(),
    onChatClick: (String, String) -> Unit = { _, _ -> }
) {

    val backgroundColor = Color(0xFFF5F8FD)
    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF555B6B)
    val blue = Color(0xFF087CF0)

    val chats = listOf(
        ChatItem(
            id = "1",
            name = "Белая LADA",
            message = "Белая LADA едет в сторону центра города.",
            time = "18:42",
            unreadCount = 2,
            isCar = true
        ),

        ChatItem(
            id = "18472",
            name = "Очевидец 18472",
            message = "Нарушение ПДД на перекрестке Ленина и Советской.",
            time = "18:31"
        ),

        ChatItem(
            id = "19231",
            name = "Очевидец 19231",
            message = "ДТП без пострадавших, нужна помощь",
            time = "17:58"
        ),

        ChatItem(
            id = "17711",
            name = "Очевидец 17711",
            message = "Стоит по встречной на парковке.",
            time = "17:42"
        ),

        ChatItem(
            id = "20394",
            name = "Очевидец 20394",
            message = "Очень опасное вождение на трассе.",
            time = "15:49"
        )
    )

    val visibleChats = chats.filterNot { chat ->
        chat.id in hiddenChatIds
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 28.dp)
            ) {

                Spacer(
                    modifier = Modifier.height(36.dp)
                )

                Text(
                    text = "Чаты",
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 23.sp,
                    color = darkText
                )

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                visibleChats.forEachIndexed { index, chat ->

                    val displayChat = chat.copy(
                        name = aliases[chat.id] ?: chat.name
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

                    if (index != visibleChats.lastIndex) {
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                    }
                }
            }

            BottomChatNavigation(
                blue = blue
            )
        }
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
            .height(88.dp)
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
            verticalAlignment = Alignment.CenterVertically
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

                Text(
                    text = chat.name,
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = darkText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = chat.message,
                    fontFamily = Commissioner,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = grayText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = chat.time,
                    fontFamily = Commissioner,
                    fontSize = 10.sp,
                    color = grayText
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
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = chat.unreadCount.toString(),
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
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Очевидец",
            tint = blue,
            modifier = Modifier.size(30.dp)
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
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "🚗",
            fontSize = 25.sp
        )
    }
}


@Composable
private fun BottomChatNavigation(
    blue: Color
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Чаты",
                tint = blue,
                modifier = Modifier.size(25.dp)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Чаты",
                fontFamily = Commissioner,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = blue
            )
        }
    }
}