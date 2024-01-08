package com.rooted.deviceinfo.web_base

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

open class WebConnector {

    /* Interceptor */
    private class NetworkInterceptor: Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
                .newBuilder()
                .addHeader("Content-Type","application/json")
                .build()
            return chain.proceed(request)
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(60L,TimeUnit.SECONDS)
        .readTimeout(60L,TimeUnit.SECONDS)
        .addInterceptor(NetworkInterceptor())
        .retryOnConnectionFailure(true)
        .build()

    val apiCallBack: ApiCallBack by lazy {
        Retrofit.Builder()
            .baseUrl(WebCons.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCallBack::class.java)
    }
}