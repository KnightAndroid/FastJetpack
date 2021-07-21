package com.aisier.architecture.entity

import java.io.Serializable

interface IBaseResponse<Data> : Serializable {

    val httpCode: Int

    val httpMsg: String?

    val httpData: Data?

    val isSuccess: Boolean

    var dataState: DataState?

    var error: Throwable?

    val isFailed: Boolean
        get() = !isSuccess

}

enum class DataState {
    STATE_CREATE,
    STATE_LOADING,
    STATE_SUCCESS,
    STATE_COMPLETED,
    STATE_EMPTY,
    STATE_FAILED,
    STATE_ERROR,
}