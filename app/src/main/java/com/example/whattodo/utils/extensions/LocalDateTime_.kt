package com.example.whattodo.utils.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.toStandardDate(): String {
    return this.format(DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault()))
}

fun LocalDateTime.toStandardTime(): String {
    return this.format(DateTimeFormatter.ofPattern(timeFormat, Locale.getDefault()))
}

val timeFormat = "HH:mm"
val dateFormat = "dd/MM/yyyy"


fun getLocalDateFromMillis(millis: Long): LocalDate{
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
}