package com.example.commontemplate.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.commontemplate.entity.TermEntity
import com.example.commontemplate.repository.UserCenterRepository
import com.example.retrofit_net.base.BaseViewModel
import com.example.retrofit_net.base.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class UserCenterViewModel :BaseViewModel(){
    private val repository by lazy { UserCenterRepository() }


    // 私有的 MutableStateFlow，用于在内部更新 UI 状态,只有 ViewModel 或其他控制逻辑可以改变状态。
    private val _termList = MutableStateFlow<MutableList<TermEntity>>(mutableListOf())
    // 对外暴露一个只读的 StateFlow，其他类只能收集它的值，但不能直接修改
    val termListFlow: StateFlow<MutableList<TermEntity>> get() = _termList


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

}