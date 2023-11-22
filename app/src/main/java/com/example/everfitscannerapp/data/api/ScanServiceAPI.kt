package com.example.everfitscannerapp.data.api

import com.example.everfitscannerapp.data.requests.ScanRequest
import com.example.everfitscannerapp.data.responses.ObjectBaseResponse
import com.example.everfitscannerapp.data.responses.ScanDataResponse
import com.example.everfitscannerapp.data.responses.ScanServiceResponse
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ScanServiceAPI {

    @GET("/list-service")
    suspend fun getService() : ScanServiceResponse

    @POST("/look-up")
    suspend fun getDetail(@Body scanRequest: ScanRequest) : ScanDataResponse

    @POST("/look-up")
    suspend fun getDetailJson(@Body scanRequest: ScanRequest) : ObjectBaseResponse<JsonObject>

    @GET("/search")
    suspend fun searchDetailJson(
        @Query("id") id: String,
        @Query("query") query: String
    ) : ObjectBaseResponse<List<JsonObject>>
}