package com.andrescalco.simplelauncher.core

import java.time.LocalDateTime
import java.util.Locale

// Names are spelled out here rather than taken from the phone's locale data,
// so the Home Screen reads the same whatever language the phone is set to.
private val weekdays = listOf("Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado", "Domingo")
private val months = listOf(
    "janeiro", "fevereiro", "março", "abril", "maio", "junho",
    "julho", "agosto", "setembro", "outubro", "novembro", "dezembro",
)

/** For example "Sábado, 25 de outubro" or "Domingo, 1º de março". */
internal fun ptBrDate(dateTime: LocalDateTime): String {
    val day = if (dateTime.dayOfMonth == 1) "1º" else dateTime.dayOfMonth.toString()
    return "${weekdays[dateTime.dayOfWeek.value - 1]}, $day de ${months[dateTime.monthValue - 1]}"
}

/** For example "21:05". */
internal fun ptBrTime(dateTime: LocalDateTime): String =
    String.format(Locale.ROOT, "%02d:%02d", dateTime.hour, dateTime.minute)
