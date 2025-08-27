package br.com.rbrthmn.ui.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun formatDouble(number: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat

    formatter.applyPattern("#,##0.00")
    val formattedDouble: String

    try {
        formattedDouble = formatter.format(number)
    } catch (e: ArithmeticException) {
        throw e
    }

    return formattedDouble
}

fun formatString(string: String): String {
    if (string.isBlank()) return ""

    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
    val formattedString: String
    formatter.applyPattern("#,##0.00")

    try {
        val newString = string.replace(',', '.')
        formattedString = formatter.format(newString.toDouble())
    } catch (e: NumberFormatException) {
        throw e
    } catch (e: ArithmeticException) {
        throw e
    }

    return formattedString
}

fun canBeFormatted(string: String): Boolean {
    if (string.isBlank()) return false

    return try {
        formatString(string)
        true
    } catch (e: NumberFormatException) {
        false
    } catch (e: ArithmeticException) {
        false
    }
}