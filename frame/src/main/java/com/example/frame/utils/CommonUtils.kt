package com.example.frame.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.frame.entity.PermissionEntity
import com.example.frame.entity.PermissionListEntity
import com.example.frame.storage.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.text.DecimalFormat

object CommonUtils {

    //获取当前activity或者fragment名字
    fun getCurrentClassName(): String {
        return "tag当前的class是: " + (Thread.currentThread().stackTrace
            .firstOrNull { it.className != "java.lang.Thread" }
            ?.className
            ?.substringAfterLast(".") ?: "Unknown")
    }

    //保存dataStore
    suspend fun <T> editDataStore(context: Context, key: Preferences.Key<T>, data: T) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[key] = data
            }
        }
    }

    //清除dataStore保存的数据
    suspend fun clearDataStore(context: Context) {
        context.dataStore.edit { settings ->
            settings.clear()
        }
    }

    //获取dataStore
    suspend fun <T> getDataStore(context: Context, key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data.map { preferences->
            preferences[key]
        }
    }


    /**
     * 检查多个授权是否被授权
     * 返回的是未授权的entity
     * 格式严格遵守PermissionListEntity类
     */
    fun checkSelfPermissionMultiple(context: Context, permissionList: List<PermissionListEntity>):List<PermissionListEntity>{
        val entity= mutableListOf<PermissionListEntity>()

        for (permissionData in permissionList) {
            val permission = mutableListOf<String>()

            for (item in permissionData.permission) {
                val result = ContextCompat.checkSelfPermission(context, item)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permission.add(item)
                }
            }

            if (permission.isNotEmpty()) {
                val data = PermissionListEntity(permission, permissionData.dataList)
                entity.add(data)
            }
        }
        return entity
    }

    /**
     * 权限说明只要调用这一行代码哦
     * 返回了2个List
     * 分别代表：explainList= mutableListOf<PermissionEntity>()里面包含了title和 msg
     *         requireList=mutableListOf<String>申请的权限列表
     */
    fun processPermissions(multipleList: List<PermissionListEntity>): Pair<List<PermissionEntity>, List<String>> {
        val explainList=multipleList.map { PermissionEntity(it.dataList.title,it.dataList.msg,it.dataList.icon) }
        val requireList=multipleList.flatMap { it.permission }

        return Pair(explainList, requireList)
    }


    //更改textview字体颜色，大小
    fun setTextColor(textView: TextView, text: String, start: Int, end: Int, color: Int, type:Int= Spanned.SPAN_EXCLUSIVE_EXCLUSIVE, changeSize:Boolean=false, size:Int=12) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            type
        )
        if (changeSize){
            spannableString.setSpan(
                AbsoluteSizeSpan(size),
                start,
                end,
                type
            )
        }

        textView.text = spannableString
    }

    //保留2位小数，计算正确率等百分比，如 "正确率: ${CommonUtils.getNoMoreDigits(homeWorkCenterEntity.correctRate.times(100))}%"
    fun getNoMoreDigits(number: Double): String {
        val format = DecimalFormat("#0.##")
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }


    //判断是否是视频
    fun isVideoUri(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.startsWith("video/") == true
    }
}