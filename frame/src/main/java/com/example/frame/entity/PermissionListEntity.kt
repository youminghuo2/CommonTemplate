package com.example.frame.entity

import android.os.Parcel
import android.os.Parcelable

data class PermissionListEntity(
    val permission:List<String>,
    val dataList: PermissionEntity
)

data class PermissionEntity(
    val title: String,
    val msg: String,
    val icon: Int? = null  // 将 icon 设为可选（默认为 null）
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,  // 读取 title
        parcel.readString()!!,  // 读取 msg
        parcel.readValue(Int::class.java.classLoader) as? Int // 读取 icon，允许 null
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)  // 写入 title
        parcel.writeString(msg)    // 写入 msg
        if (icon != null) {
            parcel.writeInt(icon) // 如果 icon 不为 null，写入它
        } else {
            parcel.writeInt(-1) // 如果 icon 为 null，写入一个占位值（-1）
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PermissionEntity> {
        override fun createFromParcel(parcel: Parcel): PermissionEntity {
            return PermissionEntity(parcel)  // 创建 PermissionEntity 实例
        }

        override fun newArray(size: Int): Array<PermissionEntity?> {
            return arrayOfNulls(size)  // 返回一个空数组
        }
    }
}