package com.example.commontemplate

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dylanc.longan.context
import com.dylanc.longan.doOnClick
import com.example.commontemplate.common.ComDaraStore
import com.example.commontemplate.databinding.ActivityMainBinding
import com.example.commontemplate.viewmodel.MainViewModel
import com.example.frame.utils.CommonUtils
import com.example.retrofit_net.base.BaseViewBindingActivity

import kotlinx.coroutines.launch


class MainActivity : BaseViewBindingActivity<ActivityMainBinding,MainViewModel>() {

    override fun initView() {
        super.initView()
    }

    override fun initData() {
        super.initData()
        val keyServerUrl = stringPreferencesKey(ComDaraStore.server_url)
        lifecycleScope.launch {
            CommonUtils.editDataStore(context, keyServerUrl,"https://xuece-xqdsj-stagingtest1.unisolution.cn/api/" )
        }
        val keyToken=stringPreferencesKey(ComDaraStore.server_token)
        lifecycleScope.launch {
            CommonUtils.editDataStore(context, keyToken,"20e395ca-bc28-453f-92f1-8ea109bafc57" )
        }
    }

    override fun initClick() {
        super.initClick()
        binding.btnInterface.doOnClick(clickIntervals = 500){
            viewModel.getTermInfo(6)
        }
    }

    override fun initObserver() {
        super.initObserver()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // 在 STARTED 状态开始收集
                viewModel.termListFlow.collect { termList ->
                   binding.tvInterface.text=termList.toString()
                }
            }
        }
    }
}