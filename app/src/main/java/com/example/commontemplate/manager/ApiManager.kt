package com.example.commontemplate.manager


import androidx.datastore.preferences.core.stringPreferencesKey
import com.dylanc.longan.Logger
import com.dylanc.longan.application
import com.dylanc.longan.context
import com.dylanc.longan.logDebug
import com.example.commontemplate.common.ComDaraStore
import com.example.frame.storage.dataStore
import com.example.frame.utils.CommonUtils
import com.example.retrofit_net.manager.HttpManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


object ApiManager {
    //获取本地的服务器地址,这个是针对多个头部的
    private suspend fun getBaseUrlFromDataStore(livingUrl: Boolean=false): Pair<String, String> {
        val urlKey = if (livingUrl) stringPreferencesKey(ComDaraStore.live_url) else stringPreferencesKey(ComDaraStore.server_url)
        val tokenKey = stringPreferencesKey(ComDaraStore.server_token)

        val urlAndToken =
            application.context.dataStore.data.map { preferences ->
                Pair(
                    preferences[urlKey] ?: "",
                    preferences[tokenKey] ?: ""
                )
            }.first()
        return urlAndToken
    }

    /**
     * userCenterApi：多个头部地址的
     */

    private fun <T> apiDeferred(livingUrl: Boolean, apiClass: Class<T>) = flow {
        val urlAndToken= getBaseUrlFromDataStore(livingUrl)
        val baseUrl = urlAndToken.first
        val token= urlAndToken.second

        Logger(CommonUtils.getCurrentClassName()).logDebug("baseUrl地址是：$baseUrl")
        Logger(CommonUtils.getCurrentClassName()).logDebug("token是：$token")
//        Logger(CommonUtils.getCurrentClassName()).logDebug("token是：$token")

//        Logger.getLogger(CommonUtils.getCurrentClassName()).logDebug("baseUrl地址是：$baseUrl")

        emit(HttpManager.create(baseUrl,token, apiClass))
    }.flowOn(Dispatchers.IO)


    //适配多个头部的
    suspend fun <T> api(apiClass: Class<T>,livingUrl: Boolean = false): T = apiDeferred(livingUrl, apiClass).first()
}