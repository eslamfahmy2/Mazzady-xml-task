package com.example.mazzadytask.presentation.utils

import com.example.mazzadytask.domain.models.UniversalError


data class ObserverCallbacks<T>(val liveData: StateLiveData<T>,
                                val success: (data:T?) -> Unit,
                                val error: (error: UniversalError?) -> Unit,
                                val loading: () -> Unit = {},
                                val complete: () -> Unit = {},
)