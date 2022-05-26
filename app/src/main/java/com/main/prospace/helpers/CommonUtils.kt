package com.main.prospace.helpers

object CommonUtils {
    const val REGEX_NAME_VALIDATOR = "^[a-zA-Z. ]+$"
    const val REGEX_PHONE_NUMBER_VALIDATOR = "^([\\-\\s]?)?[0]?(91)?[123456789]\\d{9}+$"
    const val REGEX_EMAIL_VALIDATOR = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+$"
    const val REGEX_PASSWORD_VALIDATOR = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$"
    const val REGEX_EMPTY_VALUE = "^(?![\\s\\S])"

    const val ADMIN = "ADMIN"
    const val USER = "USER"
    const val SELECT_CHARACTER_TYPE = "Select personality"
    const val AMBIVERT = "Ambivert"
    const val INTROVERT = "Introvert"
    const val EXTROVERT = "Extrovert"

    val CHARACTER_TYPE_ARRAY = arrayOf(
        SELECT_CHARACTER_TYPE, AMBIVERT, INTROVERT, EXTROVERT
    )

    const val EDIT_TEXT = "Edit Text"
    const val RATING_BAR = "Rating Bar"
    const val SEEK_BAR = "Seek Bar"
    const val RADIO_BUTTON = "Radio Button"
}