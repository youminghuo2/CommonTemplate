package com.example.frame.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


abstract class BaseViewBindingDialogFragment<VB : ViewBinding> : AppCompatDialogFragment(),
    View.OnClickListener {
    private var _binding: VB? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 获取 View Binding 类的 Class 对象
        val vbClass = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<*>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        _binding = method.invoke(this, inflater) as VB
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.5f)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    open fun initView() {}

    open fun initData() {}

    override fun onClick(v: View?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}