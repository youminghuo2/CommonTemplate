package com.example.commontemplate.viewmodel

import com.example.commontemplate.entity.LoginEntity
import com.example.commontemplate.entity.TermEntity
import com.example.commontemplate.repository.UserCenterRepository
import com.example.retrofit_net.base.BaseViewModel
import com.example.retrofit_net.base.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel :BaseViewModel(){
    private val repository by lazy { UserCenterRepository() }

    // 私有的 MutableStateFlow，用于在内部更新 UI 状态,只有 ViewModel 或其他控制逻辑可以改变状态。
    private val _termList = MutableStateFlow<MutableList<TermEntity>>(mutableListOf())
    // 对外暴露一个只读的 StateFlow，其他类只能收集它的值，但不能直接修改
    val termListFlow: StateFlow<MutableList<TermEntity>> get() = _termList

    private val _loginInfo=MutableStateFlow<LoginEntity?>(null)
    val loginInfoFlow: StateFlow<LoginEntity?> get() = _loginInfo

    fun getTermInfo(schoolId:Long){
        launchFlow(
            flowAsyncWork = {
                   repository.getTermInfo(schoolId)
                },
            collector = {resultState ->
                // 处理 ResultState 中的不同状态
                when (resultState) {
                    is ResultState.Success -> {
                        // 处理成功的情况
                        _termList.value= mutableListOf()   //flow只有在数据变化的时候，collect才收集，为此每次发送一个空的list让数据变化
                        _termList.value = resultState.data ?: mutableListOf()
                    }
                    is ResultState.Error -> {
                        // 处理错误的情况
                        val errorMsg = resultState.exception
                        errorMsgLiveData.value=errorMsg
                    }
                }

            }
        )
    }

    fun getLoginInfo(){
        launchFlow(
            flowAsyncWork = {
                repository.getLoginInfo()
            },
            collector = {resultState ->
                // 处理 ResultState 中的不同状态
                when (resultState) {
                    is ResultState.Success -> {
                        // 处理成功的情况
                        _loginInfo.value=resultState.data
                    }
                    is ResultState.Error -> {
                        // 处理错误的情况
                        val errorMsg = resultState.exception
                        errorMsgLiveData.value=errorMsg
                    }
                }

            }
        )
    }

}