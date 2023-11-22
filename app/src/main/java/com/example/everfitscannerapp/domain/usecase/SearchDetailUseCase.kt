package com.example.everfitscannerapp.domain.usecase

import com.example.everfitscannerapp.domain.repository.ScanRepository
import com.google.gson.JsonObject
import javax.inject.Inject

class SearchDetailUseCase @Inject constructor(private val scanRepository: ScanRepository) {

    suspend fun getScanDetailJs(serviceId: String, input: String) : List<JsonObject> {
        return scanRepository.searchDetailJson(
            serviceId = serviceId,
            input = input
        )
    }

}