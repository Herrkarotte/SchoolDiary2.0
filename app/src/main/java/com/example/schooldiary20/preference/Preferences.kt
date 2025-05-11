package com.example.schooldiary20.preference

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Preferences @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    var userId: String
        get() = prefs.getString("user_id", "") ?: ""
        set(value) = prefs.edit().putString("user_id", value).apply()

    var userRole: String
        get() = prefs.getString("user_role", "") ?: ""
        set(value) = prefs.edit().putString("user_role", value).apply()

    var userToken: String
        get() = prefs.getString("user_token", "") ?: ""
        set(value) = prefs.edit().putString("user_token", value).apply()

    var userClass: String
        get() = prefs.getString("user_class", "") ?: ""
        set(value) = prefs.edit().putString("user_class", value).apply()

    fun clear() = prefs.edit().clear().apply()
}