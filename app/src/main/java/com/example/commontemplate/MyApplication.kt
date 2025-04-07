package com.example.commontemplate

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import com.alibaba.sdk.android.push.CloudPushService
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initPushSDK(this)
    }

    private fun initPushSDK(context: Context){
        PushServiceFactory.init(this)
        PushServiceFactory.getCloudPushService().setDebug(true)
        PushServiceFactory.getCloudPushService().setLogLevel(CloudPushService.LOG_DEBUG)
        PushServiceFactory.getCloudPushService().setNotificationSmallIcon(R.mipmap.ic_launcher)
        createNotificationChannel()
//        HuaWeiRegister.register(this) // 接入华为辅助推送
    }

    fun createNotificationChannel() {
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // 通知渠道的id。
        val id = "8.0up"

        // 用户可以看到的通知渠道的名字。
        val name: CharSequence = "notification channel"

        // 用户可以看到的通知渠道的描述。
        val description = "notification description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(id, name, importance)

        // 配置通知渠道的属性。
        mChannel.description = description

        // 设置通知出现时的闪灯（如果Android设备支持的话）。
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED

        // 设置通知出现时的震动（如果Android设备支持的话）。
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        // 最后在notificationmanager中创建该通知渠道。
        mNotificationManager.createNotificationChannel(mChannel)


    }
}