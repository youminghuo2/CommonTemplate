package com.example.commontemplate.flutter

import android.content.ContentValues
import android.content.Intent
import android.net.Uri

import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.content.FileProvider

import com.dylanc.longan.Logger
import com.dylanc.longan.context
import com.dylanc.longan.doOnClick
import com.dylanc.longan.logDebug

import com.example.commontemplate.databinding.ActivityFlutterViewBinding
import com.example.frame.base.BaseViewBindingActivity
import com.example.frame.base.BaseViewModel
import com.example.frame.camera.CameraHelper

import com.example.frame.camera.PreviewCallback
import com.example.frame.utils.CommonUtils
import java.io.File
import java.io.Serializable

class FlutterViewActivity : BaseViewBindingActivity<ActivityFlutterViewBinding, BaseViewModel>() {
    private lateinit var photoURI: Uri
//    private lateinit var pictureSelectorUtil: PictureSelectorUtil
    private var fileName = ""

    override fun initClick() {
        super.initClick()

        //跳转系统拍照，假设已经授予了camera
        binding.btnSystemCamera.doOnClick(clickIntervals = 500){
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                try {
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        photoURI = createTempPictureUri()
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        takePhotoLauncher.launch(photoURI)
                    }

                } catch (e: Exception) {
                    Logger(CommonUtils.getCurrentClassName()).logDebug("\"error ---> ${e.message}")
                }
            }
        }

        //跳转自定义拍照
        binding.btnSettingCamera.doOnClick(clickIntervals = 500) {
            CameraHelper.startPreviewActivity(context, "com.example.commontemplate.fileprovider",MyPreviewCallback())
        }


    }

    //跳转系统拍照
    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
            if (isSaved) {
                //保存成功
                Toast.makeText(context, "我收到了系统的消息，可以开始旋转了", Toast.LENGTH_SHORT)
                    .show()
                saveImageToGallery(photoURI)
            }
        }

    private fun saveImageToGallery(imageUri: Uri) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    contentResolver.openInputStream(imageUri)?.use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }

        // 广播媒体扫描
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri))
    }

    //根据fileprovider获取uri
    fun createTempPictureUri(
        fileName: String = "picture_${System.currentTimeMillis()}",
        fileExtension: String = ".png"
    ): Uri {
        val tempFile =
            File.createTempFile(fileName, fileExtension, cacheDir).apply { createNewFile() }
        this.fileName = fileName

        return FileProvider.getUriForFile(
            this,
            "com.example.commontemplate.fileprovider",
            tempFile
        )
    }

    companion object {
        class MyPreviewCallback: PreviewCallback, Serializable {
            override fun onPreviewFinished(url: String) {
                // 处理回调，例如使用 Log 打印 URL
                Log.d("MyPreviewCallback", "Received image URL: $url")

            }
        }
    }
}