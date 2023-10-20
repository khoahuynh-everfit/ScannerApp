package com.example.everfitscannerapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everfitscannerapp.domain.model.ScanService
import com.example.everfitscannerapp.domain.model.ScannedData
import com.example.everfitscannerapp.domain.usecase.GetScanDetailInfoUseCase
import com.example.everfitscannerapp.domain.usecase.GetServiceUseCase
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getServiceUseCase: GetServiceUseCase,
    private val getScanDetailInfoUseCase: GetScanDetailInfoUseCase
) : ViewModel() {

    var callingAPI: Boolean by mutableStateOf(false)

    var scannedInfoJson : String by mutableStateOf("")
        private set

    var scannedInfo : ScannedData by mutableStateOf(ScannedData())
        private set

    var scanServices : List<ScanService> by mutableStateOf(listOf())
        private set

    fun getServices() {
        viewModelScope.launch(Dispatchers.IO) {
            val listServices = getServiceUseCase.getService()
            scanServices = listServices
        }
    }

    fun selectService(serviceId: String) {
        scanServices.forEach { s -> s.isSelected = s.id == serviceId }
    }

    fun getDataInfo(barcode: Barcode) {
        scanServices.firstOrNull { s -> s.isSelected }?.let { scanService ->
            viewModelScope.launch(Dispatchers.IO) {
                val detail = getScanDetailInfoUseCase.getScanDetail(scanService.id, scanCode = barcode.rawValue?:"")
                scannedInfo = detail
            }
        }
    }

    fun getDataInfoJson(barcode: Barcode) {
        callingAPI = true
        scannedInfoJson = ""
        scanServices.firstOrNull { s -> s.isSelected }?.let { scanService ->
            viewModelScope.launch(Dispatchers.IO) {
                scannedInfoJson = getScanDetailInfoUseCase.getScanDetailJs(scanService.id, scanCode = barcode.rawValue?:"")
                callingAPI = false
            }
        }
    }

    fun scanFail(e: Exception) {

    }

    fun scanCancel() {

    }

    fun safeExecute() {

    }

}