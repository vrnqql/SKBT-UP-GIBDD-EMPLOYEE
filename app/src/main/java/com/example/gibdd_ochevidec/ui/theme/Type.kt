package com.example.gibdd_ochevidec.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gibdd_ochevidec.R

val Commissioner = FontFamily(
    Font(
        resId = R.font.commissioner_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.commissioner_semibold,
        weight = FontWeight.SemiBold
    )
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Commissioner,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = Commissioner,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    bodySmall = TextStyle(
        fontFamily = Commissioner,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

    titleLarge = TextStyle(
        fontFamily = Commissioner,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),

    titleMedium = TextStyle(
        fontFamily = Commissioner,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),

    labelLarge = TextStyle(
        fontFamily = Commissioner,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
)