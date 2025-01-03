package com.example.frame.utils

object CommonUtils {

    fun getCurrentClassName(): String {
        return "tag当前的class是: " + (Thread.currentThread().stackTrace
            .firstOrNull { it.className != "java.lang.Thread" }
            ?.className
            ?.substringAfterLast(".") ?: "Unknown")
    }

}