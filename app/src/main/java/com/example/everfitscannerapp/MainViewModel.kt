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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getServiceUseCase: GetServiceUseCase,
    private val getScanDetailInfoUseCase: GetScanDetailInfoUseCase
) : ViewModel() {

    var callingAPI: Boolean by mutableStateOf(false)

    var scannedInfoJson : HashMap<String, String> by mutableStateOf(hashMapOf())
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

    fun searchDataInfoJson(barcodeStr: String) {
        callingAPI = true
        scannedInfoJson = hashMapOf()
        val deferreds = hashMapOf<ScanService, Deferred<String>>()
        viewModelScope.launch(Dispatchers.IO) {
            scanServices.forEach { scanService ->
                deferreds[scanService] = async {
                    getScanDetailInfoUseCase.getScanDetailJs(
                        scanService.id,
                        scanCode = barcodeStr
                    )
                }
            }
            val scannedData = hashMapOf<String, String>()
            deferreds.forEach { data ->
                scannedData[data.key.name] = data.value.await()
            }
            scannedInfoJson = scannedData
            callingAPI = false
        }
    }

    fun scanFail(e: Exception) {

    }

    fun scanCancel() {

    }

    fun safeExecute() {

    }

}