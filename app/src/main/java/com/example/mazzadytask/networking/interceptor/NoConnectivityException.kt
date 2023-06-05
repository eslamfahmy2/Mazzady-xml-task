package com.example.mazzadytask.networking.interceptor

import android.content.Context
import com.example.mazzadytask.R
import java.io.IOException

class NoConnectivityException(val context: Context) : IOException() {
    override val message: String
        get() = context.getString(R.string.no_internet_connection_error)
}