package com.example.everfitscannerapp.domain.usecase

import com.example.everfitscannerapp.domain.model.ScanService
import com.example.everfitscannerapp.domain.repository.ScanRepository
import javax.inject.Inject

class GetServiceUseCase @Inject constructor(private val scanRepository: ScanRepository) {

    suspend fun getService() : List<ScanService> {
        return scanRepository.getScanService()
    }

}