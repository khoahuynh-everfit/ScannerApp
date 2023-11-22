package com.example.everfitscannerapp.domain.usecase

import com.example.everfitscannerapp.domain.model.ScannedData
import com.example.everfitscannerapp.domain.repository.ScanRepository
import com.google.gson.JsonObject
import javax.inject.Inject

class GetScanDetailInfoUseCase @Inject constructor(private val scanRepository: ScanRepository) {

    suspend fun getScanDetail(serviceId: String, scanCode: String) : ScannedData {
        return scanRepository.getScanDetail(
            serviceId = serviceId,
            scanCode = scanCode
        )
    }

    suspend fun getScanDetailJs(serviceId: String, scanCode: String) : JsonObject? {
        return scanRepository.getScanDetailJson(
            serviceId = serviceId,
            scanCode = scanCode
        )
    }

}