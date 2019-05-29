package com.example.fcast.data.Responce

import com.google.gson.annotations.SerializedName


data class ResponseData(
    @SerializedName("current")
    val current: Current,
    val location: Location
)