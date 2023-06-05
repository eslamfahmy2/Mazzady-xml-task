package com.example.mazzadytask.networking.wrappers

class WsException(
    override val message: String?,
    val status: WsResponseWrapper.WsStatus? = null,
    val statusCode: Int? = null,
) : Exception()


