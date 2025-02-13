package com.example.frame.camera

import java.io.Serializable

interface PreviewCallback : Serializable {
    fun onPreviewFinished(url: String)
}