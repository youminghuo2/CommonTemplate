package com.example.frame.camera

import java.io.Serializable



interface SelectPhotoCallback : Serializable {
    fun onPreviewFinished(url:List<String>)
}