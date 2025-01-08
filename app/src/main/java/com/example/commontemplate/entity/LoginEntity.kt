package com.example.commontemplate.entity


data class LoginEntity(
    val authtoken: String,
    val jwtuuid: String,
    val openId: String,
    val policyVersion: PolicyVersion,
    val showModifyPwd: Boolean,
    val showPolicy: Boolean,
    val showRemindBind: Boolean,
    val user: User
)

data class PolicyVersion(
    val KID_PRIVACY_PROTECTION_POLICY: Int,
    val PRIVACY_POLICY: Int,
    val USER_AGREEMENT: Int
)

data class User(
    val id: Int,
    val initpwd: Boolean,
    val isActivated: Boolean,
    val name: String,
    val phoneNumber: String,
    val role: String,
    val schoolId: Int,
    val securityCode: String,
    val teacherCourseCode: String,
    val username: String,
    val usertypeCode: String
)
