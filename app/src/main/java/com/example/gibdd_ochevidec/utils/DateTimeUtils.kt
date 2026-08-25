package com.example.gibdd_ochevidec.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


private val russianLocale = Locale("ru", "RU")


private fun parseServerDateTime(
    value: String
): ZonedDateTime? {

    val zone = ZoneId.systemDefault()

    return runCatching {

        OffsetDateTime
            .parse(value)
            .atZoneSameInstant(zone)

    }.recoverCatching {

        Instant
            .parse(value)
            .atZone(zone)

    }.recoverCatching {

        LocalDateTime
            .parse(value)
            .atZone(zone)

    }.getOrNull()
}


// Время внутри сообщения
fun formatMessageTime(
    value: String
): String {

    val dateTime =
        parseServerDateTime(value)
            ?: return ""

    return dateTime.format(
        DateTimeFormatter.ofPattern(
            "HH:mm"
        )
    )
}


// Дата + время в списке чатов
fun formatChatListDateTime(
    value: String
): String {

    val dateTime =
        parseServerDateTime(value)
            ?: return ""

    val zone =
        ZoneId.systemDefault()

    val messageDate =
        dateTime.toLocalDate()

    val today =
        LocalDate.now(zone)

    val yesterday =
        today.minusDays(1)

    val time =
        dateTime.format(
            DateTimeFormatter.ofPattern(
                "HH:mm"
            )
        )


    return when (messageDate) {

        today -> {
            "Сегодня, $time"
        }

        yesterday -> {
            "Вчера, $time"
        }

        else -> {

            val date =
                messageDate.format(
                    DateTimeFormatter.ofPattern(
                        "dd.MM"
                    )
                )

            "$date, $time"
        }
    }
}


// Разделитель внутри чата
fun formatMessageDateDivider(
    value: String
): String {

    val dateTime =
        parseServerDateTime(value)
            ?: return ""

    val zone =
        ZoneId.systemDefault()

    val messageDate =
        dateTime.toLocalDate()

    val today =
        LocalDate.now(zone)

    val yesterday =
        today.minusDays(1)


    return when (messageDate) {

        today -> {
            "Сегодня"
        }

        yesterday -> {
            "Вчера"
        }

        else -> {

            messageDate.format(
                DateTimeFormatter.ofPattern(
                    "d MMMM",
                    russianLocale
                )
            )
        }
    }
}