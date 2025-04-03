package com.example.commontemplate

import android.app.Application
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.dylanc.longan.Logger
import com.dylanc.longan.context
import com.dylanc.longan.logDebug
import com.example.frame.utils.CommonUtils

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        PushServiceFactory.init(context)

        val pushService = PushServiceFactory.getCloudPushService()
        pushService.register(this, object : com.alibaba.sdk.android.push.CommonCallback {
            override fun onSuccess(success: String) {
                Logger(CommonUtils.getCurrentClassName()).logDebug("已经授权")
            }
            override fun onFailed(errorCode: String, errorMessage: String) {
                Logger(CommonUtils.getCurrentClassName()).logDebug("已经授权")
            }
        })
    }
}