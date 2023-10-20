package com.example.everfitscannerapp.data.requests

import com.google.gson.annotations.SerializedName

data class ScanRequest(
    @SerializedName("id") val id: String,
    @SerializedName("code") val code: String
)
