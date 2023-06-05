package com.example.mazzadytask.networking.wrappers

import com.example.mazzadytask.R
import com.google.gson.Gson
import com.example.mazzadytask.utils.ApplicationContextSingleton.getString
import retrofit2.Response


class WsResponseWrapper<T> {
    var data: T? = null
    var code: Int = 0
    var requestStatus = WsStatus.values()[code]
    var msg: String? = null

    enum class WsStatus {
        SUCCESS, NOT_FOUND, INVALID_ARGUMENT, OPERATION_FAILED
    }
}

suspend fun <T> unwrapResponse(response: Response<WsResponseWrapper<T>>): T? {
    val body = response.body()
    if (response.isSuccessful && body != null) {
        if (body.requestStatus == WsResponseWrapper.WsStatus.SUCCESS) {
            return body.data
        } else {
            throw parseError(body.requestStatus, body.code, body.msg)
        }
    } else {
        val errorBody = response.errorBody()
        if (errorBody != null) {
            var errorThrowable = WsException(
                getString(R.string.unknown_error),
                WsResponseWrapper.WsStatus.OPERATION_FAILED
            )
            try {
                val errorObject: WsResponseWrapper<T> =
                    Gson().fromJson(errorBody.charStream(), WsResponseWrapper<T>()::class.java)
                errorThrowable =
                    parseError(errorObject.requestStatus, errorObject.code, errorObject.msg)
            } catch (e: Exception) {
                throw errorThrowable
            }
            throw errorThrowable
        } else {
            if (response.message().isNullOrEmpty()) {
                throw WsException(
                    getString(R.string.unknown_error),
                    WsResponseWrapper.WsStatus.OPERATION_FAILED
                )
            } else {
                throw WsException(response.message(), WsResponseWrapper.WsStatus.OPERATION_FAILED)
            }
        }
    }
}

fun parseError(
    status: WsResponseWrapper.WsStatus,
    statusCode: Int,
    errorMessages: String?
): WsException {
    return if (errorMessages.isNullOrEmpty()) {
        WsException(getString(R.string.unknown_error), status, statusCode)
    } else {
        WsException(errorMessages, status, statusCode)
    }
}
