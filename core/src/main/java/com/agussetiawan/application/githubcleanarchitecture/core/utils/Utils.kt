package com.agussetiawan.application.githubcleanarchitecture.core.utils

import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.response.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody

fun parseErrorBody(errorBody: ResponseBody?): ErrorResponse{

    val gson = Gson()
    val type = object : TypeToken<ErrorResponse>() {}.type
    var errorResponse: ErrorResponse? = gson.fromJson(errorBody!!.charStream(), type)

    return errorResponse!!
}