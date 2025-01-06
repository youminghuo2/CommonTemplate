package com.example.retrofit_net.manager

import com.dylanc.longan.Logger
import com.dylanc.longan.logDebug
import com.example.frame.utils.CommonUtils
import com.example.retrofit_net.interceptor.HeaderInterceptor
import com.example.retrofit_net.interceptor.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    fun createRetrofit(baseUrl: String, token: String): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            // 打印日志到控制台（可以自定义日志输出方式）
            Logger(CommonUtils.getCurrentClassName()).logDebug("RetrofitLog:$$message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY // 打印完整的请求和响应
        }

        val mOkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor (HeaderInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(TokenInterceptor())
            .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}

fun <T> create(baseUrl: String, token: String, service: Class<T>): T {
    val mRetrofit = RetrofitFactory.createRetrofit(baseUrl, token)
    return mRetrofit.create(service)
}
