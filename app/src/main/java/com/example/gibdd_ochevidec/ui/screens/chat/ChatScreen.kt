package com.example.gibdd_ochevidec.ui.screens.chat

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gibdd_ochevidec.ui.theme.Commissioner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class ChatMessageUi(
    val id: String,
    val text: String,
    val time: String,
    val isEmployee: Boolean
)


private val ReplyTemplates = listOf(
    "Уточните марку автомобиля.",
    "Уточните цвет автомобиля.",
    "Уточните государственный номер.",
    "Уточните направление движения.",
    "Вы можете отправить фото или видео?",
    "Уточните местоположение.",
    "Спасибо, информация принята."
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatName: String = "Белая LADA",
    chatId: String = "18472",
    messages: List<ChatMessageUi> = emptyList(),
    onBackClick: () -> Unit = {},
    onTemplateSend: (String) -> Unit = {},
    onAliasChanged: (String) -> Unit = {},
    onBlockConfirmed: () -> Unit = {}
) {

    val backgroundColor = Color(0xFFF5F8FD)
    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF626776)
    val blue = Color(0xFF087CF0)

    var showReplySheet by remember {
        mutableStateOf(false)
    }

    var showMenu by remember {
        mutableStateOf(false)
    }

    var showBlockDialog by remember {
        mutableStateOf(false)
    }

    var showAliasDialog by remember {
        mutableStateOf(false)
    }

    var aliasText by remember {
        mutableStateOf(chatName)
    }

    val displayedMessages = remember {
        mutableStateListOf<ChatMessageUi>()
    }

    LaunchedEffect(messages) {
        displayedMessages.clear()
        displayedMessages.addAll(messages)
    }

    val listState = rememberLazyListState()

    LaunchedEffect(displayedMessages.size) {
        if (displayedMessages.isNotEmpty()) {
            listState.animateScrollToItem(
                displayedMessages.lastIndex
            )
        }
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

            // ШАПКА
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(
                        start = 22.dp,
                        end = 22.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = darkText,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = chatName,
                        fontFamily = Commissioner,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = darkText
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "ID $chatId",
                        fontFamily = Commissioner,
                        fontSize = 13.sp,
                        color = grayText
                    )
                }

                Box {

                    IconButton(
                        onClick = {
                            showMenu = true
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Меню",
                            tint = darkText,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = {
                            showMenu = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Заблокировать очевидца",
                                    fontFamily = Commissioner,
                                    fontSize = 13.sp
                                )
                            },
                            onClick = {
                                showMenu = false
                                showBlockDialog = true
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Изменить псевдоним",
                                    fontFamily = Commissioner,
                                    fontSize = 13.sp
                                )
                            },
                            onClick = {
                                showMenu = false
                                aliasText = chatName
                                showAliasDialog = true
                            }
                        )
                    }
                }
            }

            HorizontalDivider(
                color = Color(0xFFE7ECF3),
                thickness = 1.dp
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(
                    start = 28.dp,
                    end = 28.dp,
                    top = 20.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = displayedMessages,
                    key = { message ->
                        message.id
                    }
                ) { message ->

                    ChatMessageCard(
                        message = message,
                        darkText = darkText,
                        grayText = grayText
                    )
                }
            }

            Button(
                onClick = {
                    showReplySheet = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = blue
                )
            ) {

                Text(
                    text = "Ответить",
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = Color.White
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }
    }


    // ШАБЛОНЫ ОТВЕТА
    if (showReplySheet) {

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        ModalBottomSheet(
            onDismissRequest = {
                showReplySheet = false
            },
            sheetState = sheetState,
            containerColor = backgroundColor
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 28.dp,
                        end = 28.dp,
                        bottom = 26.dp
                    )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Ответить",
                        fontFamily = Commissioner,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 23.sp,
                        color = darkText,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            showReplySheet = false
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть"
                        )
                    }
                }

                ReplyTemplates.forEachIndexed { index, template ->

                    ReplyTemplateCard(
                        text = template,
                        darkText = darkText,
                        onClick = {

                            val currentTime =
                                SimpleDateFormat(
                                    "HH:mm",
                                    Locale.getDefault()
                                ).format(Date())

                            displayedMessages.add(
                                ChatMessageUi(
                                    id = System.currentTimeMillis().toString(),
                                    text = template,
                                    time = currentTime,
                                    isEmployee = true
                                )
                            )

                            onTemplateSend(template)

                            showReplySheet = false
                        }
                    )

                    if (index != ReplyTemplates.lastIndex) {
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                    }
                }
            }
        }
    }


    // БЛОКИРОВКА
    if (showBlockDialog) {

        Dialog(
            onDismissRequest = {
                showBlockDialog = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.84f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Вы действительно хотите\nзаблокировать данного очевидца?",
                        fontFamily = Commissioner,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        lineHeight = 19.sp,
                        color = darkText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(15.dp)
                    )

                    Text(
                        text = "Пользователь не сможет отправлять\nсообщения в течение 1 суток.",
                        fontFamily = Commissioner,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = grayText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(26.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Button(
                            onClick = {
                                showBlockDialog = false
                                onBlockConfirmed()
                            },
                            modifier = Modifier
                                .weight(0.85f)
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = blue
                            )
                        ) {

                            Text(
                                text = "Да",
                                fontFamily = Commissioner,
                                color = Color.White
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                showBlockDialog = false
                            },
                            modifier = Modifier
                                .weight(1.15f)
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                blue
                            )
                        ) {

                            Text(
                                text = "Отмена",
                                fontFamily = Commissioner,
                                color = blue
                            )
                        }
                    }
                }
            }
        }
    }


    // ПСЕВДОНИМ
    if (showAliasDialog) {

        Dialog(
            onDismissRequest = {
                showAliasDialog = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.84f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Text(
                        text = "Изменить псевдоним",
                        fontFamily = Commissioner,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = darkText
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Text(
                        text = "Локальный псевдоним",
                        fontFamily = Commissioner,
                        fontSize = 12.sp,
                        color = grayText
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    OutlinedTextField(
                        value = aliasText,
                        onValueChange = {
                            aliasText = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        textStyle = TextStyle(
                            fontFamily = Commissioner,
                            fontSize = 13.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = blue,
                            unfocusedBorderColor = blue,
                            cursorColor = blue
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text = "Псевдоним будет отображаться только на этом устройстве",
                        fontFamily = Commissioner,
                        fontSize = 10.sp,
                        color = grayText
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Button(
                            onClick = {

                                val newAlias =
                                    aliasText.trim()

                                if (newAlias.isNotEmpty()) {
                                    onAliasChanged(newAlias)
                                    showAliasDialog = false
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = blue
                            )
                        ) {

                            Text(
                                text = "Сохранить",
                                fontFamily = Commissioner,
                                color = Color.White
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                showAliasDialog = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                blue
                            )
                        ) {

                            Text(
                                text = "Отмена",
                                fontFamily = Commissioner,
                                color = blue
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ReplyTemplateCard(
    text: String,
    darkText: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = darkText,
                modifier = Modifier.size(23.dp)
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Text(
                text = text,
                fontFamily = Commissioner,
                fontSize = 13.sp,
                color = darkText
            )
        }
    }
}


@Composable
private fun ChatMessageCard(
    message: ChatMessageUi,
    darkText: Color,
    grayText: Color
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            if (message.isEmployee) {
                Arrangement.End
            } else {
                Arrangement.Start
            }
    ) {

        Card(
            modifier = Modifier.widthIn(
                min = 120.dp,
                max = 310.dp
            ),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    if (message.isEmployee) {
                        Color(0xFFE5F1FF)
                    } else {
                        Color.White
                    }
            )
        ) {

            Column(
                modifier = Modifier.padding(
                    start = 18.dp,
                    end = 14.dp,
                    top = 14.dp,
                    bottom = 10.dp
                )
            ) {

                Text(
                    text = message.text,
                    fontFamily = Commissioner,
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    color = darkText
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Text(
                    text = message.time,
                    fontFamily = Commissioner,
                    fontSize = 10.sp,
                    color = grayText,
                    modifier = Modifier.align(
                        Alignment.End
                    )
                )
            }
        }
    }
}