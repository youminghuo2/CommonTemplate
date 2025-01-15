package com.example.frame.utils


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import com.dylanc.longan.dp
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel

fun shapeBackground(
    fillColor: Int = Color.WHITE,
    borderWidth: Float = 0f,
    borderColor: Int = Color.GRAY,
    allCorner: Float = 0f,
    topLeftCorner: Float = 0f,
    topRightCorner: Float = 0f,
    bottomLeftCorner: Float = 0f,
    bottomRightCorner: Float = 0f,
): MaterialShapeDrawable {
    val shapeAppearanceModel = ShapeAppearanceModel.builder().apply {
        setAllCorners(RoundedCornerTreatment())
        setTopLeftCornerSize(if (topLeftCorner == 0f) allCorner.dp else topLeftCorner.dp)
        setTopRightCornerSize(if (topRightCorner == 0f) allCorner.dp else topRightCorner.dp)
        setBottomLeftCornerSize(if (bottomLeftCorner == 0f) allCorner.dp else bottomLeftCorner.dp)
        setBottomRightCornerSize(if (bottomRightCorner == 0f) allCorner.dp else bottomRightCorner.dp)
    }.build()

    val drawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
        setTint(fillColor)
        paintStyle = Paint.Style.FILL_AND_STROKE
        strokeWidth = borderWidth.dp
        strokeColor = ColorStateList.valueOf(borderColor)
    }
    return drawable
}