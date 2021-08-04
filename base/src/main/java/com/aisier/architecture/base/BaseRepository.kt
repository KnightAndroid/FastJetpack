package com.aisier.architecture.base

import com.aisier.architecture.entity.BaseResponse
import com.aisier.architecture.entity.DataState
import com.aisier.architecture.entity.IBaseResponse
import com.aisier.architecture.net.StateLiveData
import com.aisier.architecture.net.handlingApiExceptions
import com.aisier.architecture.net.handlingExceptions
import kotlinx.coroutines.delay

open class BaseRepository {
    /**
     * repo 请求数据的公共方法，
     * 在不同状态下先设置 baseResp.dataState的值，最后将dataState 的状态通知给UI
     * @param block api的请求方法
     * @param stateLiveData 每个请求传入相应的LiveData，主要负责网络状态的监听
     */
    suspend fun <T : Any> executeResp(stateLiveData: StateLiveData<T>, block: suspend () -> IBaseResponse<T>) {
        var baseResp: IBaseResponse<T> = BaseResponse()
        stateLiveData.postLoading(baseResp)
        //for test
        delay(500)
        runCatching {
            baseResp = block.invoke()
        }.onSuccess {
            handleHttpOkResponse(baseResp)
        }.onFailure { e ->
            e.printStackTrace()
            //非后台返回错误，捕获到的异常
            stateLiveData.setError(baseResp, e)
            handlingExceptions(e)
        }
        stateLiveData.postValue(baseResp)
    }

    /**
     * Http 状态码200，请求成功，但是后台定义了一些错误码
     */
    private fun <T : Any> handleHttpOkResponse(baseResp: IBaseResponse<T>) {
        if (baseResp.isSuccess) {
            if (baseResp.httpData == null || baseResp.httpData is List<*> && (baseResp.httpData as List<*>).isEmpty()) {
                //TODO: 数据为空,结构变化时需要修改判空条件
                baseResp.dataState = DataState.STATE_EMPTY
            } else {
                baseResp.dataState = DataState.STATE_SUCCESS
            }
        } else {
            handlingApiExceptions(baseResp.httpCode, baseResp.httpMsg)
            baseResp.dataState = DataState.STATE_FAILED
        }
    }


}