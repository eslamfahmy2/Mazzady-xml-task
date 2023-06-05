package com.example.mazzadytask.utils


import com.example.mazzadytask.R
import com.example.mazzadytask.networking.interceptor.NoConnectivityException
import com.example.mazzadytask.networking.wrappers.WsException
import com.example.mazzadytask.domain.models.UniversalError
import com.example.mazzadytask.domain.state.AppOperationState
import com.example.mazzadytask.domain.state.StateData
import com.example.mazzadytask.domain.state.universalError
import com.example.mazzadytask.domain.state.unknownError
import com.example.mazzadytask.presentation.utils.StateLiveData

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/*
    Extension function used to wrap network calls in order to provide universal error handling.
    This should be used for all network calls.
 */
fun <T : Any?> CoroutineScope.safeLaunch(
    request: StateLiveData<T> = StateLiveData(),
    isDataPersistent: Boolean = false,
    launchBody: suspend () -> Unit,
): Job {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        // handle thrown exceptions from coroutine scope
        when (exception) {
            //If an exception or error occurs during coroutine execution it will come here.
            // Note that all code in the coroutine after the error instance will not execute!
            is IOException -> {
                when (exception) {
                    is UnknownHostException -> {
                        request.value =
                            StateData<T>().error(UniversalError(getBadInternetErrorMessage(exception.localizedMessage)))
                    }
                    is ConnectException -> request.value =
                        StateData<T>().error(UniversalError(getBadInternetErrorMessage(exception.localizedMessage)))
                    is NoConnectivityException -> request.value =
                        StateData<T>().error(UniversalError(getBadInternetErrorMessage(exception.localizedMessage)))
                    is SocketTimeoutException -> request.value =
                        StateData<T>().error(UniversalError(ApplicationContextSingleton.getString(R.string.internet_timeout_error)))
                    else -> {
                        request.value =
                            StateData<T>().error(UniversalError(exception.localizedMessage))
                    }
                }
            }

            is WsException -> request.value =
                StateData<T>().error(UniversalError(exception.message))

            is HttpException -> request.value =
                StateData<T>().getHttpError(exception.response())

            else -> if (!exception.localizedMessage.isNullOrEmpty()) {
                request.value = StateData<T>().error(UniversalError(exception.localizedMessage))
            } else {
                request.value = StateData<T>().unknownError()
            }
        }

        request.postComplete()

        //Reset data at the end if persistence is not needed
        if (!isDataPersistent) {
            request.value = StateData()
        }
    }

    return this.launchIdling(coroutineExceptionHandler) {
        request.postLoading()
        launchBody.invoke()
        request.postComplete()

        //Reset data at the end if persistence is not needed
        if (!isDataPersistent) {
            request.value = StateData()
        }
    }
}

/*
    Extension function used to wrap network calls in order to provide universal error handling.
    This should be used for all network calls.
 */
fun CoroutineScope.safeLaunchWithFlow(
    sharedFlow: MutableStateFlow<AppOperationState>,
    launchBody: suspend () -> Unit,
): Job {

    val scope = this
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        // handle thrown exceptions from coroutine scope
        when (exception) {
            //If an exception or error occurs during coroutine execution it will come here.
            // Note that all code in the coroutine after the error instance will not execute!
            is NoConnectivityException ->
                scope.launch {
                    sharedFlow.emit(universalError(UniversalError(exception.localizedMessage)))
                }

            is ConnectException ->
                scope.launch {
                    sharedFlow.emit(universalError(UniversalError(exception.localizedMessage)))
                }

            is SocketTimeoutException ->
                scope.launch {
                    sharedFlow.emit(universalError(UniversalError(exception.localizedMessage)))
                }

            is IOException -> {
                when (exception) {
                    is UnknownHostException -> {
                        scope.launch {
                            sharedFlow.emit(
                                universalError(
                                    UniversalError(
                                        getBadInternetErrorMessage(
                                            exception.localizedMessage
                                        )
                                    )
                                )
                            )
                        }
                    }
                    else -> {
                        scope.launch {
                            sharedFlow.emit(universalError(UniversalError(exception.localizedMessage)))
                        }
                    }
                }
            }

            is HttpException ->
                scope.launch {
                    sharedFlow.emit(universalError(UniversalError(exception.localizedMessage)))
                }

            else -> if (!exception.localizedMessage.isNullOrEmpty()) {
                scope.launch {
                    sharedFlow.emit(universalError(UniversalError(exception.localizedMessage)))
                }
            } else {
                scope.launch {
                    sharedFlow.tryEmit(unknownError())
                }
            }
        }
    }

    return this.launchIdling(coroutineExceptionHandler) {
        sharedFlow.emit(AppOperationState(error = null, AppOperationState.DataStatus.LOADING))
        launchBody.invoke()
        sharedFlow.emit(AppOperationState(error = null, AppOperationState.DataStatus.COMPLETE))
    }
}

fun getBadInternetErrorMessage(message: String?): String {
    val bottomMessage =
        ApplicationContextSingleton.getString(R.string.no_internet_connection_error_coroutine)
    return message ?: ("" + "\n\n" + bottomMessage)
}

/*
    Extension function used to attach idling resource listener for testing purposes
 */
fun CoroutineScope.launchIdling(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
   // EspressoIdlingResource.increment()
    val job = this.launch(context, start, block)
    job.invokeOnCompletion {
      //  EspressoIdlingResource.decrement()
    }
    return job
}