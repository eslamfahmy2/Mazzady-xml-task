package com.example.mazzadytask.domain.state

import com.example.mazzadytask.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.mazzadytask.domain.models.UniversalError
import com.example.mazzadytask.utils.ApplicationContextSingleton
import okhttp3.ResponseBody
import retrofit2.Response


class StateData<T> {
    var status: DataStatus
        private set
    var data: T?
        private set
    var error: UniversalError?
        private set

    fun loading(): StateData<T> {
        status = DataStatus.LOADING
        data = null
        error = null
        return this
    }

    fun success(data: T?): StateData<T> {
        status = DataStatus.SUCCESS
        this.data = data
        error = null
        return this
    }

    fun error(universalError: UniversalError): StateData<T> {
        status = DataStatus.ERROR
        data = null
        this.error = universalError
        return this
    }

    enum class DataStatus {
        SUCCESS, ERROR, LOADING , COMPLETE
    }

    init {
        status = DataStatus.LOADING
        data = null
        error = null
    }

    fun getHttpError(response: Response<*>?): StateData<T> {
        if (response == null) {
            return unknownError()
        }
        return if (response.code() >= 500) {
            error(serverError(response))
        } else {
            val errorBody = response.errorBody()
            if (errorBody != null) {
                getErrorFromBody(errorBody)
            } else {
                unknownError()
            }
        }
    }

    private fun serverError(response: Response<*>): UniversalError {
        var additionalInfo = ""
        if (!response.message().isNullOrEmpty()) {
            additionalInfo = response.message()
        }
        return UniversalError(
            ApplicationContextSingleton.getString(
                R.string.server_error_message,
                additionalInfo
            )
        )
    }

    fun unknownError(): StateData<T> {
        return error(unknownDomainError())
    }

    private fun unknownDomainError(): UniversalError {
        return UniversalError(
            ApplicationContextSingleton.getString(
                R.string.unknown_error
            )
        )
    }

    private fun getErrorFromBody(responseBody: ResponseBody?): StateData<T> {
        if (responseBody == null || responseBody.string().isEmpty()) {
            return unknownError()
        }
        val parsedError: UniversalError?
        try {
            parsedError = Gson().getAdapter(TypeToken.get(UniversalError::class.java))
                .fromJson(responseBody.string())
        } catch (exception: Exception) {
            return unknownError()
        }
        return if (parsedError != null) {
            error(parsedError)
        } else {
            unknownError()
        }
    }

    fun complete(data: T?): StateData<T> {
        status = DataStatus.COMPLETE
        this.data = data
        return this
    }
}