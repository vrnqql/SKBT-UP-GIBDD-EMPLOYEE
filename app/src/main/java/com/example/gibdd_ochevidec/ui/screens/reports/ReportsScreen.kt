package com.example.gibdd_ochevidec.ui.screens.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.foundation.BorderStroke

enum class ReportScreenState {
    MAIN,
    LOADING,
    READY
}


@Composable
fun ReportsScreen(
    state: ReportScreenState = ReportScreenState.MAIN,
    errorText: String? = null,

    onGenerateClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onLoadingBackClick: () -> Unit = {},

    onChatsClick: () -> Unit = {},
    onEmployeesClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {

    when (state) {

        ReportScreenState.MAIN -> {

            ReportsMainScreen(
                errorText = errorText,
                onGenerateClick = onGenerateClick,
                onChatsClick = onChatsClick,
                onEmployeesClick = onEmployeesClick,
                onNotificationsClick = onNotificationsClick
            )
        }


        ReportScreenState.LOADING -> {

            ReportLoadingScreen(
                onBackClick = onLoadingBackClick
            )
        }


        ReportScreenState.READY -> {

            ReportReadyScreen(
                onDownloadClick = onDownloadClick,
                onCloseClick = onCloseClick
            )
        }
    }
}


// =====================================================
// 1. ОСНОВНОЙ ЭКРАН "ОТЧЁТЫ"
// =====================================================

@Composable
private fun ReportsMainScreen(
    errorText: String?,
    onGenerateClick: () -> Unit,
    onChatsClick: () -> Unit,
    onEmployeesClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {

    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF5F6372)
    val blue = Color(0xFF087CF0)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    start = 28.dp,
                    end = 28.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(34.dp)
            )


            Text(
                text = "Отчёты",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 23.sp,
                color = darkText
            )


            Spacer(
                modifier = Modifier.height(15.dp)
            )


            Text(
                text = "Скачайте единый Excel-файл с отчётами\nза весь период",
                fontFamily = Commissioner,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                color = darkText
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =============================================
            // КАРТОЧКА С ОПИСАНИЕМ EXCEL
            // =============================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 18.dp,
                            top = 18.dp,
                            end = 18.dp,
                            bottom = 18.dp
                        )
                ) {

                    Text(
                        text = "Excel-файл содержит 3 листа:",
                        fontFamily = Commissioner,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = darkText
                    )


                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )


                    ReportSection(
                        title = "Баны",
                        description =
                            "Дата и время, ID сотрудника, выдавшего бан,\nID заблокированного пользователя",
                        darkText = darkText,
                        grayText = grayText
                    )


                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )


                    ReportSection(
                        title = "Роли",
                        description =
                            "Дата и время, ID сотрудника, действие с ролью,\nID устройства",
                        darkText = darkText,
                        grayText = grayText
                    )


                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )


                    ReportSection(
                        title = "Сообщения",
                        description =
                            "ID сотрудника и количество отправленных\nим сообщений",
                        darkText = darkText,
                        grayText = grayText
                    )


                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )


                    // XLSX-плашка справа
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF87DDB1),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .background(
                                    color = Color(0xFFE9F9F0),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(
                                    horizontal = 7.dp,
                                    vertical = 3.dp
                                )
                        ) {

                            Text(
                                text = "XLSX",
                                fontFamily = Commissioner,
                                fontSize = 9.sp,
                                color = Color(0xFF45B97C)
                            )
                        }
                    }
                }
            }


            if (!errorText.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(14.dp)
                )


                Text(
                    text = errorText,
                    fontFamily = Commissioner,
                    fontSize = 12.sp,
                    color = Color(0xFFD34A4A)
                )
            }


            Spacer(
                modifier = Modifier.weight(1f)
            )


            // =============================================
            // КНОПКА "СКАЧАТЬ ОТЧЁТ"
            // =============================================

            Button(
                onClick = onGenerateClick,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                shape = RoundedCornerShape(13.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = blue
                )
            ) {

                Text(
                    text = "Скачать отчёт",
                    fontFamily = Commissioner,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }


            Spacer(
                modifier = Modifier.height(13.dp)
            )
        }


        AppBottomNavigation(
            selectedTab = MainTab.REPORTS,

            showEmployees = true,
            showNotifications = true,
            showReports = true,

            onChatsClick = onChatsClick,
            onEmployeesClick = onEmployeesClick,
            onNotificationsClick = onNotificationsClick,

            onReportsClick = {
                // Уже на вкладке "Отчёты"
            }
        )
    }
}


@Composable
private fun ReportSection(
    title: String,
    description: String,
    darkText: Color,
    grayText: Color
) {

    Column {

        Text(
            text = title,
            fontFamily = Commissioner,
            fontSize = 11.sp,
            color = Color(0xFF979BA7)
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        Text(
            text = description,
            fontFamily = Commissioner,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            color = darkText
        )
    }
}


// =====================================================
// 2. ФОРМИРОВАНИЕ ОТЧЁТА
// =====================================================

@Composable
private fun ReportLoadingScreen(
    onBackClick: () -> Unit
) {

    val darkText = Color(0xFF090B22)
    val blue = Color(0xFF087CF0)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {

        // ШАПКА
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 22.dp,
                    end = 28.dp,
                    top = 27.dp
                ),

            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(32.dp)
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,

                    contentDescription = "Назад",
                    tint = darkText,
                    modifier = Modifier.size(20.dp)
                )
            }


            Spacer(
                modifier = Modifier.width(10.dp)
            )


            Text(
                text = "Формирование отчёта",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = darkText
            )
        }


        Column(
            modifier = Modifier.align(
                Alignment.Center
            ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator(
                modifier = Modifier.size(72.dp),
                color = blue,
                strokeWidth = 6.dp
            )


            Spacer(
                modifier = Modifier.height(48.dp)
            )


            Text(
                text = "Формируем отчёт...",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = darkText
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            Text(
                text = "Пожалуйста, подождите",
                fontFamily = Commissioner,
                fontSize = 16.sp,
                color = Color(0xFF5F6372)
            )
        }
    }
}


// =====================================================
// 3. ОТЧЁТ ГОТОВ
// =====================================================

@Composable
private fun ReportReadyScreen(
    onDownloadClick: () -> Unit,
    onCloseClick: () -> Unit
) {

    val darkText = Color(0xFF090B22)
    val grayText = Color(0xFF626776)
    val blue = Color(0xFF087CF0)


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
            modifier = Modifier.height(34.dp)
        )


        Text(
            text = "Отчёт готов",
            fontFamily = Commissioner,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = darkText
        )


        Spacer(
            modifier = Modifier.weight(0.78f)
        )


        // ЗЕЛЁНЫЙ КРУГ С ГАЛОЧКОЙ
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

            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF05B85C),
                modifier = Modifier.size(48.dp)
            )
        }


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        Text(
            text = "Отчёт сформирован",
            fontFamily = Commissioner,
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp,
            color = darkText
        )


        Spacer(
            modifier = Modifier.height(6.dp)
        )


        Text(
            text = "Файл готов к скачиванию",
            fontFamily = Commissioner,
            fontSize = 14.sp,
            color = grayText
        )


        Spacer(
            modifier = Modifier.weight(1f)
        )


        Button(
            onClick = onDownloadClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape = RoundedCornerShape(13.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = blue
            )
        ) {

            Text(
                text = "Скачать",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
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

            shape = RoundedCornerShape(13.dp),

            border = BorderStroke(
                width = 1.dp,
                color = blue
            ),

            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = blue
            )
        ) {

            Text(
                text = "Закрыть",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )
    }
}