package com.example.retrofit_net.base

open class BaseResult<T>(
    val data: T?,
    val code: String,
    val subCode: String,
    val msg: String,
    val msgDetail: String,
) {
    fun isSuccess(): Boolean {
        return code == "SUCCESS"
    }
}