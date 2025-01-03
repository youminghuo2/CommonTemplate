package com.example.retrofit_net.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frame.entity.DialogBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class BaseViewModel :ViewModel(){
    // LiveData 用于显示/隐藏加载对话框
    val showLoadingDialog: MutableLiveData<DialogBean> by lazy { MutableLiveData<DialogBean>() }
    val errorMsgLiveData by lazy { MutableLiveData<String>() }

    fun showLoading(strMessage: String = "正在请求数据") {
        val showBean = showLoadingDialog.value ?: DialogBean(strMessage, true)
        showBean.isShow = true
        showBean.msg = strMessage
        showLoadingDialog.postValue(showBean)
    }

    // 隐藏加载对话框
    fun hideLoading() {
        val showBean = showLoadingDialog.value ?: DialogBean("", true)
        showBean.isShow = false
        showLoadingDialog.postValue(showBean)
    }

    // ViewModel 销毁时取消所有协程任务
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    // 扩展 Flow：切换到 IO 线程
    private fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)

    // 扩展 Flow：启动时显示加载对话框
    private fun <T> Flow<T>.onStartAndShow(strMessage: String = "正在请求数据"): Flow<T> = this.onStart {
        showLoading()
    }

    // 扩展 Flow：完成时隐藏加载对话框
    private fun <T> Flow<T>.onCompletionAndHide(): Flow<T> = this.onCompletion {
        hideLoading()
    }

    // 扩展 Flow：完成链式操作（启动、切换线程、完成状态）
    private suspend fun <T> Flow<T>.onStartShowFlowOnIOAndOnCompletionAndCollect(
        collector: suspend (T) -> Unit
    ) {
        this.onStartAndShow()
            .onCompletionAndHide()
            .flowOnIO()
            .collect(collector)
    }

    // 启动 ViewModelScope 的协程任务并执行指定 Flow 操作
    fun <T> launchFlow(flowAsyncWork: suspend () -> Flow<T>, collector: suspend (T) -> Unit) {
        viewModelScope.launch {
            flowAsyncWork.invoke().onStartShowFlowOnIOAndOnCompletionAndCollect(collector)
        }
    }

    // 启动 ViewModelScope 的协程任务并执行自定义逻辑
    fun launchCoroutine(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block.invoke(this)
        }
    }
}