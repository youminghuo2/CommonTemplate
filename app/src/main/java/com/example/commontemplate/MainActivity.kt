package com.example.commontemplate

import android.Manifest
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dylanc.longan.Logger
import com.dylanc.longan.activityresult.launch
import com.dylanc.longan.activityresult.registerForLaunchAppSettingsResult
import com.dylanc.longan.activityresult.registerForRequestMultiplePermissionsResult
import com.dylanc.longan.activityresult.registerForRequestPermissionResult
import com.dylanc.longan.context
import com.dylanc.longan.doOnClick
import com.dylanc.longan.isPermissionGranted
import com.dylanc.longan.launchAppSettings
import com.dylanc.longan.logDebug
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.commontemplate.common.ComDaraStore
import com.example.commontemplate.databinding.ActivityMainBinding
import com.example.commontemplate.flutter.FlutterViewActivity
import com.example.commontemplate.viewmodel.MainViewModel
import com.example.frame.base.BaseViewBindingActivity
import com.example.frame.dialog.flutterDialog.FlutterDialogFragment
import com.example.frame.dialog.loadingDialog.LoadingDialogFragment
import com.example.frame.dialog.permissionDialog.PermissionExplainHelper.dismissExplain
import com.example.frame.dialog.permissionDialog.PermissionExplainHelper.showExplain
import com.example.frame.dialog.singleDialog.CommonDialogBuilder
import com.example.frame.dialog.singleDialog.CommonDialogFragment
import com.example.frame.entity.PermissionEntity
import com.example.frame.entity.PermissionListEntity
import com.example.frame.storage.dataStore
import com.example.frame.utils.CommonUtils
import com.example.frame.utils.CommonUtils.processPermissions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class MainActivity : BaseViewBindingActivity<ActivityMainBinding, MainViewModel>() {
    private var isTaskPaused = false // 用于暂停或恢复定时任务
    private val keyServerUrl = stringPreferencesKey(ComDaraStore.server_url)  //dataSore的key
    private var dialogMsg = "" //dialog提示
    private var loadingDialog: LoadingDialogFragment? = null   //LoadingFragment弹窗


    override fun initData() {
        super.initData()
        initNotificaitonChannel()

        //存储dataStore
        lifecycleScope.launch {
            CommonUtils.editDataStore(
                context,
                keyServerUrl,
                "https://xuece-xqdsj-stagingtest1.unisolution.cn/api/"
            )
        }

    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun initClick() {
        super.initClick()

        //读取dataStore
        binding.btnDataStore.doOnClick(clickIntervals = 500) {
            val usertypeCodeFlow: Flow<String> =
                context.dataStore.data.map { preferences ->
                    preferences[keyServerUrl] ?: ""
                }
            lifecycleScope.launch {
                usertypeCodeFlow.collect { token ->
                    binding.tvInterface.text = token
                }
            }
        }

        //登录接口，对应entity接收类型
        binding.btnInterfaceLogin.doOnClick(clickIntervals = 500) {
            viewModel.getLoginInfo()
        }
        //对应List接收类型
        binding.btnInterfaceTerm.doOnClick(clickIntervals = 500) {
            viewModel.getTermInfo(6)
        }

        // 全局单例dialog代码
        binding.btnSingleDialog.doOnClick(clickIntervals = 500) {
            isTaskPaused = false
            lifecycleScope.launch {
                delay(2000)
                // 定时每 1 秒执行一次
                while (isActive) { // 确保协程处于活跃状态
                    if (!isTaskPaused) {
                        CommonDialogBuilder(this@MainActivity, true)
                            .setTitle("标题")
                            .setMessage("正文")
                            .setNegativeButton("取消")
                            .setPositiveButton(
                                text = "确定",
                                listener = object : CommonDialogFragment.OnClickListener {
                                    override fun onClick(dialog: Dialog?) {
                                        dialog?.dismiss()
                                        // 暂停任务
                                        isTaskPaused = true
                                    }
                                }).show()
                    }
                    // 延时 1 秒
                    delay(1000)
                }
            }
        }

        //普通dialog
        binding.btnCommonDialog.doOnClick(clickIntervals = 500) {
            CommonDialogBuilder(this@MainActivity)
                .setTitle("标题", titleColor = Color.parseColor("#5197ff"))
                .setNegativeButton("取消")
                .setPositiveButton(
                    "确定",
                    listener = object : CommonDialogFragment.OnClickListener {
                        override fun onClick(dialog: Dialog?) {
                            dialog?.dismiss()
                        }

                    })
                .setMessage("正文").show()
        }

        //申请单个权限
        binding.btnRequestPermission.doOnClick(clickIntervals = 500) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            when (isPermissionGranted(permission)) {
                true -> {
                    //授权
                    toast("已经授予权限")
                }

                false -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

        //申请多个权限
        binding.btnRequestPermissions.doOnClick(clickIntervals = 1000) {
            val permissionList = listOf(
                PermissionListEntity(
                    listOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ),
                    PermissionEntity(
                        "蓝牙权限",
                        "当您在我们的产品中使蓝牙",
                        R.drawable.ic_perm_bluetooth
                    )
                ),
                PermissionListEntity(
                    listOf(Manifest.permission.CAMERA),
                    PermissionEntity(
                        "相机权限",
                        "当您在我们的产品中使用数据上传功能时",
                        R.drawable.ic_perm_camera
                    )
                ),
                PermissionListEntity(
                    listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PermissionEntity(
                        "地理位置权限",
                        "当您在我们的产品中使用地理位置权限",
                        R.drawable.ic_perm_location
                    )
                ),
                PermissionListEntity(
                    listOf(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE,
                    ),
                    PermissionEntity(
                        "媒体权限",
                        "当您在我们的产品中使用媒体权限",
                        R.drawable.ic_perm_storage
                    )
                )
            )

            val multipleList = CommonUtils.checkSelfPermissionMultiple(
                context,
                permissionList
            )
            if (multipleList.isNotEmpty()) {
                val (explainList, requireList) = processPermissions(multipleList)

                val msg = explainList.joinToString(separator = ",") { it.title }
                dialogMsg = "请在设置中开启${msg}，以正常使用App功能"

                showExplain(supportFragmentManager, explainList)

                multiPermissionLauncher.launch(
                    requireList.toTypedArray()
                )
            }

        }

        //flutter dialog弹窗
        binding.btnFlutterDialog.doOnClick(clickIntervals = 500) {
            FlutterDialogFragment(
                "权限申请",
                dialogMsg,
                "取消",
                "确定",
                false,
                object : FlutterDialogFragment.OnClickListener {
                    override fun onClick(dialog: Dialog?) {
                        dialog?.dismiss()
                        launchAppSettings()
                    }
                }).show(supportFragmentManager, "")
        }

        //LoadingFragment弹窗
        binding.btnLoadingDialog.doOnClick(clickIntervals = 500) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialogFragment()
                loadingDialog?.isCancelable = true
                loadingDialog!!.show(supportFragmentManager, "")
            } else {
                loadingDialog?.dismiss()
                loadingDialog = null
            }
        }

        //跳转flutter功能页
        binding.btnLoadingDialog.doOnClick(clickIntervals = 500) {
            startActivity<FlutterViewActivity>()
        }

    }

    override fun initObserver() {
        super.initObserver()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // 在 STARTED 状态开始收集
                viewModel.termListFlow
                    .filter { it.isNotEmpty() } // 过滤掉空值
                    .collect { termList ->
                        binding.tvInterface.text = termList.toString()
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // 在 STARTED 状态开始收集
                viewModel.loginInfoFlow
                    .filter { it != null } // 过滤掉空值
                    .collect { loginInfo ->
                        binding.tvInterface.text = loginInfo.toString()

                        loginInfo?.let {
                            val keyToken = stringPreferencesKey(ComDaraStore.server_token)
                            lifecycleScope.launch {
                                CommonUtils.editDataStore(context, keyToken, it.authtoken)
                            }
                        }

                    }
            }
        }
    }


    /**
     * 权限
     */
    private val requestPermissionLauncher = registerForRequestPermissionResult(
        onGranted = {
            // 已同意
        },
        onDenied = {
            // 拒绝且不再询问，可弹框引导用户到设置里授权该权限
            // 弹框提示后可调用 launchAppSettings() 方法跳到设置页
            appSettingsLauncher.launch()  // 启动应用设置页面
        },
        onShowRequestRationale = {
            // 拒绝了一次，可弹框解释为什么要获取该权限
            // 弹框提示后可调用 requestPermissionAgain() 方法重新请求
        })


    private val appSettingsLauncher = registerForLaunchAppSettingsResult {
    }

    //多个权限
    private val multiPermissionLauncher = registerForRequestMultiplePermissionsResult(
        onAllGranted = {
            // 已全部同意
        },
        onDenied = { deniedList ->
            // 部分权限已拒绝且不再询问，可弹框引导用户到设置里授权该权限
            // 弹框提示后可调用 launchAppSettings() 方法跳到设置页
            dismissExplain()
            CommonDialogBuilder(this@MainActivity)
                .setTitle("权限申请", titleColor = Color.parseColor("#5197ff"))
                .setMessage("权限申请")
                .setCenter(true)
                .setNegativeButton("关闭")
                .setPositiveButton(
                    "确定",
                    listener = object : CommonDialogFragment.OnClickListener {
                        override fun onClick(dialog: Dialog?) {
                            dialog?.dismiss()
                            com.dylanc.longan.launchAppSettings()
                        }
                    })
                .show()

        },
        onShowRequestRationale = { deniedList ->
            // 部分权限拒绝了一次，可弹框解释为什么要获取该权限
            // 弹框提示后可调用 requestDeniedPermissions() 方法请求拒绝的权限
        })

    fun initNotificaitonChannel() {
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // 通知渠道的id。
        val id = "1"

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

        when (mNotificationManager.areNotificationsEnabled()) {
            true -> {
                // 已经授权
                Logger(CommonUtils.getCurrentClassName()).logDebug("已经授权")
            }

            false -> {
                Logger(CommonUtils.getCurrentClassName()).logDebug("error")
            }
        }
    }


}