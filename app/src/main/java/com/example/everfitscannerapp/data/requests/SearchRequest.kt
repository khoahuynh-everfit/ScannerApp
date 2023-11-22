package com.example.everfitscannerapp.data.requests

import com.google.gson.annotations.SerializedName

data class SearchRequest(
    @SerializedName("id") val id: String,
    @SerializedName("query") val query: String
)
