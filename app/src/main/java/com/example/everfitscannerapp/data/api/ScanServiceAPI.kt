package com.example.everfitscannerapp.data.api

import com.example.everfitscannerapp.data.requests.ScanRequest
import com.example.everfitscannerapp.data.responses.ScanDataResponse
import com.example.everfitscannerapp.data.responses.ScanServiceResponse
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ScanServiceAPI {

    @GET("/list-service")
    suspend fun getService() : ScanServiceResponse

    @POST("/look-up")
    suspend fun getDetail(@Body scanRequest: ScanRequest) : ScanDataResponse

    @POST("/look-up")
    suspend fun getDetailJson(@Body scanRequest: ScanRequest) : JsonObject
}