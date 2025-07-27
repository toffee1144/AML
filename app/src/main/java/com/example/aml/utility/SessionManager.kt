package com.example.aml.utility

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

object SessionManager {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"

    fun save(context: Context, token: String, userId: String, username: String, email: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_EMAIL, email)
            apply()
        }
    }
    fun getEmail(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_EMAIL, "guest@example.com")
    }
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }


    // Pisahkan storage untuk foto profil
    private const val PROFILE_PREF = "profile_prefs"
    private const val KEY_PROFILE_PHOTO = "profile_photo_url"

    fun setProfilePhotoUrl(context: Context, url: String) {
        val prefs = context.getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PROFILE_PHOTO, url).apply()
        Log.d("SessionManager", "Saving profile photo URL: $url")
    }

    fun getProfilePhotoUrl(context: Context): String? {
        val prefs = context.getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PROFILE_PHOTO, null)
    }


    fun getUsername(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, "Guest")
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }

    fun getUserId(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_ID, DeviceIdManager.getDeviceId(context))!!
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit() { clear() }
    }
}
