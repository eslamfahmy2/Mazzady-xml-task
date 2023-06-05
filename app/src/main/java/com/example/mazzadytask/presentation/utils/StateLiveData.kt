package com.example.mazzadytask.presentation.utils

import androidx.lifecycle.MutableLiveData
import com.example.mazzadytask.domain.models.UniversalError
import com.example.mazzadytask.domain.state.StateData

class StateLiveData<T> : MutableLiveData<StateData<T>>() {
    /**
     * Use this to put the Data on a LOADING Status
     */
    fun postLoading() {
        postValue(StateData<T>().loading())
    }

    /**
     * Use this to put the Data on a ERROR DataStatus
     * @param throwable the error to be handled
     */
    fun postError(universalError: UniversalError) {
        postValue(StateData<T>().error(universalError))
    }

    /**
     * Use this to put the Data on a SUCCESS DataStatus
     * @param data
     */
    fun postSuccess(data: T) {
        postValue(StateData<T>().success(data))
    }

    /**
     * Use this to put the Data on a COMPLETE DataStatus
     */
    fun postComplete() {
        postValue(StateData<T>().complete(value?.data))
    }
}