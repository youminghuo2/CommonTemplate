package com.example.frame.dialog.singleDialog

import androidx.appcompat.app.AppCompatActivity

class CommonDialogBuilder(private val activity: AppCompatActivity, private val useInstance:Boolean=false) {

    /**
     * 通过控制 `useNewInstance` 的值来决定如何实例化 dialog
     * useInstance => {
     *          true ->  全局单例dialog
     *          false -> 常规dialog
     * }
     */
    private val dialog by lazy {
        if (useInstance) {
            SingleDialogBuilder.getDialog()
        } else {
            CommonDialogFragment() // 使用构造函数
        }
    }

    fun setTitle(title: String,titleColor: Int? = null, titleSize: Float? = null): CommonDialogBuilder {
        dialog.setTitle(title,titleColor,titleSize)
        return this

    }

    fun setMessage(message: String,messageColor: Int? = null, messageSize: Float? = null): CommonDialogBuilder {
        dialog.setMessage(message,messageColor,messageSize)
        return this
    }

    fun setNegativeButton(text: String,textColor:Int?=null,textSize:Float?=null,backgroundTint: Int?=null,cornerRadius: Int?=null, strokeColor: Int?=null, strokeWidth: Int?=null, listener: CommonDialogFragment.OnClickListener?=null): CommonDialogBuilder {
        dialog.setNegativeButton(text,textColor,textSize,backgroundTint,cornerRadius,strokeColor,strokeWidth,listener)
        return this
    }

    fun setPositiveButton(
        text: String,textColor:Int?=null,textSize:Float?=null,backgroundTint: Int?=null,cornerRadius: Int?=null, strokeColor: Int?=null, strokeWidth: Int?=null, listener: CommonDialogFragment.OnClickListener?=null
    ): CommonDialogBuilder {
        dialog.setPositiveButton(text,textColor,textSize,backgroundTint,cornerRadius,strokeColor,strokeWidth,listener)
        return this
    }

    fun setCancelable(text: String, backgroundTint: Int?, cornerRadius: Int?, strokeColor: Int?, strokeWidth: Int, listener: CommonDialogFragment.OnClickListener, cancelable: Boolean): CommonDialogBuilder {
        dialog.dialogCancelable = cancelable
        return this
    }

    fun show(tag: String = "") {
        dialog.show(activity.supportFragmentManager, "")
    }

    fun isDialogShowing() =  dialog.isAdded && dialog.isVisible

    fun show() {
        when(useInstance){
            true ->{
                if (!isDialogShowing()) {
                    dialog.show(activity.supportFragmentManager, "")
                }
            }
            false->{  dialog.show(activity.supportFragmentManager, "")}
        }
    }

    fun setCenter(isCenter: Boolean): CommonDialogBuilder {
        dialog.setDialogCenter(isCenter)
        return this
    }
}