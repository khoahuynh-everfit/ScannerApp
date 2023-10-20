package com.example.everfitscannerapp.data.responses

import com.google.gson.annotations.SerializedName

data class ScanServiceResponse(
    @SerializedName("data") val data: ScanServiceListResponse
)

data class ScanServiceListResponse(
    @SerializedName("list") val list: List<ScanServiceDetailResponse>,
    @SerializedName("page") val page: Int,
    @SerializedName("perPage") val perPage: Int,
    @SerializedName("total") val total: Int,
)

data class ScanServiceDetailResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
)