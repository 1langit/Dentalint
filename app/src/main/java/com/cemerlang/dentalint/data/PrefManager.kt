package com.cemerlang.dentalint.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefManager @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveString(key: Key, value: String) {
        editor.putString(key.name, value).apply()
    }

    fun getString(key: Key, defaultValue: String = ""): String {
        return sharedPreferences.getString(key.name, defaultValue) ?: defaultValue
    }

    fun saveInt(key: Key, value: Int) {
        editor.putInt(key.name, value).apply()
    }

    fun getInt(key: Key, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key.name, defaultValue)
    }

    fun saveBoolean(key: Key, value: Boolean) {
        editor.putBoolean(key.name, value).apply()
    }

    fun getBoolean(key: Key, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key.name, defaultValue)
    }

    fun clear() {
        editor.clear().apply()
    }

    enum class Key(name: String) {
        // user
        TOKEN("token"),
        UID("uid"),
        EMAIL("email"),
        NAME("name"),

        // settings
        NOTIFICATION("notification"),
        LANGUAGE("language"),
        THEME("theme"),
    }
}