package com.example.frame.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelLazy
import androidx.viewbinding.ViewBinding
import com.dylanc.longan.Logger
import com.dylanc.longan.logDebug
import com.dylanc.longan.toast
import com.example.frame.dialog.loadingDialog.LoadingDialog
import com.example.frame.utils.CommonUtils
import java.lang.reflect.ParameterizedType

open class BaseViewBindingActivity<VB : ViewBinding,VM : BaseViewModel> : AppCompatActivity(),
    View.OnClickListener {

     private var loading: LoadingDialog? = null
     protected val viewModel by lazyViewModels()

    protected val binding by lazy {
        getViewBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //透明状态栏
        val controller = WindowCompat.getInsetsController(window,window.decorView);
        controller.isAppearanceLightStatusBars=true
        setContentView(binding.root)

        initView()
        initData()
        initClick()
        initObserver()
    }

    private fun getViewBinding(): VB {
        val vbClass = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<*>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        val view =method.invoke(null, layoutInflater) as VB
        Logger(CommonUtils.getCurrentClassName()).logDebug("当前Fragment:${javaClass.simpleName}")
        return view
    }

    open fun initView() {}
    open fun initData() {}
    open fun initClick() {}
    open fun initObserver() {
        viewModel.run {
            showLoadingDialog.observe(this@BaseViewBindingActivity) {
                if (it.isShow) showLoading(it.msg) else hideLoading()
            }
            errorMsgLiveData.observe(this@BaseViewBindingActivity) {
                toast(it)
            }
        }
    }

    override fun onClick(v: View?) {
    }

    open fun isShowLoading():Boolean? {
        return loading?.isShowing
    }

    open fun showLoading(showText: String?) {
        if (null == loading) loading = LoadingDialog(this)
        if (isShowLoading() == true) return
        if (showText != null) loading?.show(showText)
    }

    open fun hideLoading() {
        loading?.dismiss()
        loading = null
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    @MainThread
    inline fun lazyViewModels(): Lazy<VM> {
        val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>
        return ViewModelLazy(cls.kotlin, { viewModelStore }, { defaultViewModelProviderFactory })
    }

}