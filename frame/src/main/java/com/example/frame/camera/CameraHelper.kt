package com.example.frame.camera

import android.content.Context
import android.content.Intent

object CameraHelper {

    var callback: PreviewCallback? = null

    fun startPreviewActivity(
        context: Context,
        providerAuthorities: String,
        callback: PreviewCallback? = null
    ) {
        CameraHelper.callback = callback
        val intent = Intent(context, PreviewViewActivity::class.java)
        intent.putExtra("authorities", providerAuthorities)
        context.startActivity(intent)
    }
}

