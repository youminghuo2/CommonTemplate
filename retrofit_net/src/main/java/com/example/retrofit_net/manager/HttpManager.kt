package com.example.retrofit_net.manager

object HttpManager {
    var baseUrl=""

    var token=""

    private fun setBase(baseUrl: String, token: String){
        this.baseUrl=baseUrl
        this.token=token
    }


    fun <T> create(baseUrl: String, token: String,service: Class<T>): T {
        setBase(baseUrl,token)
        //多个地址写法
        val mRetrofit = RetrofitFactory.createRetrofit(baseUrl, token)
        return mRetrofit.create(service)
    }
}