package br.com.rbrthmn.auth.common

const val MIN_PASSWORD_LENGTH = 8

private val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)

fun isValidPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
