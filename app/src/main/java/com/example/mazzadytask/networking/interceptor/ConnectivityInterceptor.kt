package com.example.mazzadytask.networking.interceptor

import android.content.Context
import com.example.mazzadytask.networking.interceptor.NetworkUtils.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityInterceptor @Inject constructor(@ApplicationContext private val applicationContext: Context) : Interceptor {
    @Throws(NoConnectivityException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable(applicationContext)) {
            throw NoConnectivityException(applicationContext)
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}