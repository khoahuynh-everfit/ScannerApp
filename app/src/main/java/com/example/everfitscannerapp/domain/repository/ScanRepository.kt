package com.example.everfitscannerapp.domain.repository

import com.example.everfitscannerapp.domain.model.ScanService
import com.example.everfitscannerapp.domain.model.ScannedData
import com.google.gson.JsonObject

interface ScanRepository {

    suspend fun getScanService() : List<ScanService>

    suspend fun getScanDetail(serviceId: String, scanCode: String) : ScannedData

    suspend fun getScanDetailJson(serviceId: String, scanCode: String) : JsonObject?

    suspend fun searchDetailJson(serviceId: String, input: String) : List<JsonObject>

}