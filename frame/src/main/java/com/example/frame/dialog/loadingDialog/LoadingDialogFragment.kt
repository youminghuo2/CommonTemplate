package com.example.frame.dialog.loadingDialog

import android.annotation.SuppressLint
import com.example.frame.databinding.FragmentLoadingDialogBinding
import com.example.retrofit_net.base.BaseViewBindingDialogFragment

/**
 * 自定义LoadingFragment
 */
class LoadingDialogFragment : BaseViewBindingDialogFragment<FragmentLoadingDialogBinding>() {

    @SuppressLint("UseCompatLoadingForDrawables")

    fun isShowing(): Boolean? {
        return dialog?.isShowing
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.0f)
    }

}