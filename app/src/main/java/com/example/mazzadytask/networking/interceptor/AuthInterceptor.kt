package com.example.mazzadytask.networking.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
            .newBuilder()
            .addHeader("private-key", "3%o8i}_;3D4bF]G5@22r2)Et1&mLJ4?\$@+16")
            .build()
        return chain.proceed(req)
    }
}