package com.example.commontemplate.api

import com.example.commontemplate.entity.TermEntity
import com.example.retrofit_net.base.BaseResult
import retrofit2.http.GET
import retrofit2.http.Query

interface UserCenterApi {
    //获取学校学期信息
    @GET("usercenter/common/loginuserinfo/terminfo")
    suspend fun getTermInfo(
        @Query("schoolId") schoolId: Long,
    ): BaseResult<MutableList<TermEntity>>
}