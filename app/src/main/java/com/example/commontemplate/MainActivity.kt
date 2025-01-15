package com.example.commontemplate

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dylanc.longan.activityresult.launch
import com.dylanc.longan.activityresult.registerForLaunchAppSettingsResult
import com.dylanc.longan.activityresult.registerForRequestPermissionResult
import com.dylanc.longan.context
import com.dylanc.longan.doOnClick
import com.dylanc.longan.isPermissionGranted
import com.dylanc.longan.launchAppSettings
import com.dylanc.longan.toast
import com.example.commontemplate.common.ComDaraStore
import com.example.commontemplate.databinding.ActivityMainBinding
import com.example.commontemplate.viewmodel.MainViewModel
import com.example.frame.dialog.permissionDialog.PermissionExplainHelper.dismissExplain
import com.example.frame.dialog.permissionDialog.PermissionExplainHelper.showExplain
import com.example.frame.dialog.singleDialog.CommonDialogBuilder
import com.example.frame.dialog.singleDialog.CommonDialogFragment
import com.example.frame.entity.PermissionEntity
import com.example.frame.entity.PermissionListEntity
import com.example.frame.storage.dataStore
import com.example.frame.utils.CommonUtils
import com.example.frame.utils.CommonUtils.processPermissions
import com.example.retrofit_net.base.BaseViewBindingActivity
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

    override fun initView() {
        super.initView()
    }

    override fun initData() {
        super.initData()

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
                            .setPositiveButton(
                                text = "确定"
                            ) { dialog ->
                                dialog?.dismiss()
                            }.show()
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
                .setMessage("正文").show()
        }

        //申请单个权限
        binding.btnRequestPermission.doOnClick(clickIntervals = 500) {
            val permission = Manifest.permission.CAMERA
            when (isPermissionGranted(permission)) {
                true -> {
                    //授权
                    toast("已经授予权限")
                }

                false -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
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
    private val multiPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            dismissExplain()
            if (it.values.any { !it }) {
                CommonDialogBuilder(this@MainActivity)
                    .setTitle("权限申请", titleColor = Color.parseColor("#5197ff"))
                    .setMessage("权限申请")
                    .setCenter(true)
                    .setNegativeButton("关闭")
                    .setPositiveButton(
                        "确定",
                    ) { dialog ->
                        dialog?.dismiss()
                        launchAppSettings()
                    }
                    .show()
                //可以在Flutter弹窗
//                FlutterDialogFragment(
//                    "权限申请",
//                    dialogMsg,
//                    "取消",
//                    "确定",
//                    false,
//                    object : FlutterDialogFragment.OnClickListener {
//                        override fun onClick(dialog: Dialog?) {
//                            dialog?.dismiss()
//                            launchAppSettings()
//                        }
//                    }).show(supportFragmentManager, "")
            }
        }
}