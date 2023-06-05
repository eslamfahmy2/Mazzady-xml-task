package com.testapp.utils.wrappers

import android.annotation.SuppressLint
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val REFRESH_TOKEN = "refresh_token"
        private const val ACCESS_TOKEN = "access_token"
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString(ACCESS_TOKEN, null) ?: ""
    }

    @SuppressLint("ApplySharedPref")
    fun saveAccessToken(token: String?) {
        //we need to save user authentication data immediately to avoid issues
        sharedPreferences.edit().putString(ACCESS_TOKEN, token).commit()
    }

    fun getRefreshToken(): String {
        return sharedPreferences.getString(REFRESH_TOKEN, null) ?: ""
    }

    @SuppressLint("ApplySharedPref")
    fun saveRefreshToken(token: String?) {
        //we need to save user authentication data immediately to avoid issues
        sharedPreferences.edit().putString(REFRESH_TOKEN, token).commit()
    }
}