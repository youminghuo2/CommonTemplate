package com.example.commontemplate.api

import com.example.commontemplate.entity.LoginEntity
import com.example.commontemplate.entity.TermEntity
import com.example.retrofit_net.base.BaseResult
import retrofit2.http.GET
import retrofit2.http.Query

interface UserCenterApi {
    //登录
    @GET("usercenter/nnauth/user/login")
    suspend fun getLoginInfo(
        @Query("clienttype")clienttype:String,
        @Query("clientversion")clientversion:String,
        @Query("encryptpwd")encryptpwd:String,
        @Query("username")username:String
    ):BaseResult<LoginEntity>

    //获取学校学期信息
    @GET("usercenter/common/loginuserinfo/terminfo")
    suspend fun getTermInfo(
        @Query("schoolId") schoolId: Long,
    ): BaseResult<MutableList<TermEntity>>
}