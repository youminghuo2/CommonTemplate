package com.example.retrofit_net.interceptor

import com.example.retrofit_net.manager.HttpManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authorised = request.newBuilder()
            .header("AuthToken", HttpManager.token)
//            .addHeader("Accept", "application/json, text/javascript, */*") //
//          .addHeader("Referer", " https://wwz.lanzouj.com/ihT5x20ulecf") //https
        if (request.header("User-Agent") == null) {
        authorised.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36") //
        }
        return chain.proceed(authorised.build())

    }
}