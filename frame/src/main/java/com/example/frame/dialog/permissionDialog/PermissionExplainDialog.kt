package com.example.frame.dialog.permissionDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frame.databinding.FragmentPermissionExplainDialogBinding
import com.example.frame.entity.PermissionEntity
import com.example.frame.utils.shapeBackground

class PermissionExplainDialog : DialogFragment() {

    private var dialogBinding: FragmentPermissionExplainDialogBinding? = null
    private val binding get() = dialogBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setType(WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogBinding = FragmentPermissionExplainDialogBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.background = shapeBackground(
            fillColor = Color.parseColor("#EAF8FF"),
            borderColor = Color.parseColor("#80BCFF"),
            borderWidth = 0.5f
        )

        val dataList =
            if (VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
                arguments?.getParcelableArrayList(
                    "permission",
                    PermissionEntity::class.java ) as List<PermissionEntity>
            }else{
                arguments?.getParcelableArrayList<PermissionEntity>("permission")
            }
        val adapter = dataList?.let { PermissionExplainAdapter(requireContext(), it) }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.0f)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.TOP)
    }

    override fun onDestroy() {
        dialogBinding = null
        super.onDestroy()
    }


    companion object {
        fun newInstance(dataList: List<PermissionEntity>): PermissionExplainDialog {
            val args = Bundle()
            args.putParcelableArrayList("permission", ArrayList(dataList))
            val fragment = PermissionExplainDialog()
            fragment.arguments = args
            return fragment
        }
    }

}