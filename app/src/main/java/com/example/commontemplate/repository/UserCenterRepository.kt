package com.example.commontemplate.repository

import com.dylanc.longan.encryptMD5
import com.example.commontemplate.api.UserCenterApi
import com.example.commontemplate.entity.LoginEntity
import com.example.commontemplate.entity.TermEntity
import com.example.commontemplate.manager.ApiManager
import com.example.retrofit_net.base.BaseRepository
import com.example.retrofit_net.base.ResultState
import kotlinx.coroutines.flow.Flow

class UserCenterRepository: BaseRepository() {

    //获取登录
    suspend fun getLoginInfo():Flow<ResultState<LoginEntity>>{
        return requestResult {
            val psd="ylx08100810"
            ApiManager.api(UserCenterApi::class.java).getLoginInfo("TCH_APP","1.7.2",psd.encryptMD5(),"testOp01")
        }
    }

    //获取学期
    suspend fun getTermInfo(schoolId:Long): Flow<ResultState<MutableList<TermEntity>>>  {
        return requestResult {
            //多个
            ApiManager.api(UserCenterApi::class.java).getTermInfo(schoolId)
        }
    }
}