package com.example.frame.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.example.frame.R


class LoadingDialog(context: Context) : Dialog(context, R.style.MaterialLoadingDialog) {

    private lateinit var txtLoadingText: TextView

    init {
        setContentView(R.layout.layout_loading)
        window?.run {
            setGravity(Gravity.CENTER)
        }
        txtLoadingText = findViewById(R.id.txt_loading_text)
    }

    fun show(showText: String) {
        txtLoadingText.apply {
            if (showText.isNotEmpty()) {
                text = showText
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
        if (!isShowing) {
            show()
        }
    }
}
