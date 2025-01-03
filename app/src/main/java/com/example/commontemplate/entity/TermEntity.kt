package com.example.commontemplate.entity

data class TermEntity(
    val year: Int,
    val termName: String,
    val status: String,
    val startDate: Long,
    val endDate: Long,
    val semesters: MutableList<SemesterEntity>,
)

data class SemesterEntity(
    val id: Int,
    val semester: String,
    val status: String,
    val startDatetime: Long,
    val endDatetime: Long,
    val startDateMultiform: String,
    val endDateMultiform: String,
)