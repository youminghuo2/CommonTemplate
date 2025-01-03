package com.example.commontemplate.repository

import com.example.commontemplate.api.UserCenterApi
import com.example.commontemplate.entity.TermEntity
import com.example.commontemplate.manager.ApiManager
import com.example.retrofit_net.base.BaseRepository
import com.example.retrofit_net.base.ResultState
import kotlinx.coroutines.flow.Flow

class UserCenterRepository: BaseRepository() {

    //获取学期，测试数据
    suspend fun getTermInfo(schoolId:Long): Flow<ResultState<MutableList<TermEntity>>>  {
        return requestResult {
            //多个
            ApiManager.api(UserCenterApi::class.java).getTermInfo(schoolId)
        }
    }
}