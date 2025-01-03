package com.example.retrofit_net.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelLazy
import androidx.viewbinding.ViewBinding
import com.dylanc.longan.toast
import com.example.frame.dialog.LoadingDialog
import java.lang.reflect.ParameterizedType

abstract class BaseViewBindingFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment(), View.OnClickListener {

    private var _binding: VB? = null
    private val binding get() = _binding!!

    private var loading: LoadingDialog? = null
    private val viewModel by lazyViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
        initClick()
        initObserver()
    }

    private fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
        val vbClass = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<*>
        val method = vbClass.getDeclaredMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return method.invoke(null, inflater, container, false) as VB
    }

    open fun initView() {}
    open fun initData() {}
    open fun initClick() {}
    open fun initObserver() {
        viewModel.run {
            showLoadingDialog.observe(viewLifecycleOwner) {
                if (it.isShow) showLoading(it.msg) else hideLoading()
            }
            errorMsgLiveData.observe(viewLifecycleOwner) {
                toast(it)
            }
        }
    }

    override fun onClick(v: View?) {}

    open fun isShowLoading(): Boolean? {
        return loading?.isShowing
    }

    open fun showLoading(showText: String?) {
        if (null == loading) loading = LoadingDialog(requireContext())
        if (isShowLoading() == true) return
        if (showText != null) loading?.show(showText)
    }

    open fun hideLoading() {
        loading?.dismiss()
        loading = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /**
     * 获取 ViewModel 的 Class 类型
     */
    @MainThread
    inline fun lazyViewModels(): Lazy<VM> {
        val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return ViewModelLazy(cls.kotlin, { viewModelStore }, { defaultViewModelProviderFactory })
    }
}
