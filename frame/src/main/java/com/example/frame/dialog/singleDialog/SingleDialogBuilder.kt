package com.example.frame.dialog.singleDialog

object SingleDialogBuilder {
    private var dialogFragment: CommonDialogFragment? = null

    fun getDialog(): CommonDialogFragment {
        if (dialogFragment == null) {
            dialogFragment = CommonDialogFragment()
        }
        return dialogFragment!!
    }
}
