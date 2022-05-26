package com.main.prospace.helpers

import android.content.Context
import android.preference.PreferenceManager
import java.util.regex.Pattern

class Preferences(context: Context) {
    private var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var editor = sharedPreferences.edit()

    fun saveUserType(userType: String) {
        editor.putString("USER_TYPE", userType)
        editor.apply()
    }

    fun getUserType(): String? {
        val userType = sharedPreferences.getString("USER_TYPE", "")
        return  userType
    }

    fun saveUserName(userName: String) {
        editor.putString("USER_NAME", userName)
        editor.apply()
    }

    fun getUserName(): String? {
        val userName = sharedPreferences.getString("USER_NAME", "")
        return  userName
    }

    fun saveUserEmail(userEmail: String) {
        editor.putString("USER_MAIL", userEmail)
        editor.apply()
    }

    fun getUserEmail(): String? {
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")
        return  userEmail
    }

    fun saveUserId(userId: Int) {
        editor.putInt("USER_ID", userId)
        editor.apply()
    }

    fun getUserId(): Int? {
        val userId = sharedPreferences.getInt("USER_ID", 0)
        return  userId
    }

    fun saveCharacterType(characterType: String) {
        editor.putString("CHARACTER_TYPE", characterType)
        editor.apply()
    }

    fun getCharacterType(): String? {
        val characterType = sharedPreferences.getString("CHARACTER_TYPE", "")
        return  characterType
    }
}