package com.example.frame.dialog.permissionDialog

import androidx.fragment.app.FragmentManager
import com.example.frame.entity.PermissionEntity

/**
 * 权限说明
 */
object PermissionExplainHelper {
    private var dialog: PermissionExplainDialog? = null

    fun showExplain(fragmentManager: FragmentManager, dataList: List<PermissionEntity>) {
        dialog = PermissionExplainDialog.newInstance(dataList)
        dialog?.show(fragmentManager, "")
    }

    fun dismissExplain() {
        dialog?.dismiss()
        dialog = null
    }

}