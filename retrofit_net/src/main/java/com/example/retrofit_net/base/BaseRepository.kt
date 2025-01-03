package com.example.retrofit_net.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

open class BaseRepository {

    suspend fun <T> requestResult(requestCall: suspend () -> BaseResult<T>?): Flow<ResultState<T>> {
        return flow {
            val result = withContext(Dispatchers.IO) {
                withTimeout(10 * 1000) {
                    requestCall()
                }
            }?: throw NoNetworkException("请求超时")

            if (result.isSuccess()){
                // 请求成功，发射成功的数据，支持 T? 类型
                emit(ResultState.Success(result.data))
            }else{
                // 如果请求失败，直接抛出异常
                // 使用 Flow 发射错误信息
                // 发射成功数据
                emit(ResultState.Error(result.msg))
            }

        }
    }
}

sealed class ResultState<out T> {
    data class Success<out T>(val data: T?) : ResultState<T>()
    data class Error(val exception: String) : ResultState<Nothing>()
}