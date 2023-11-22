package com.example.everfitscannerapp.ui.views.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.everfitscannerapp.domain.model.ResultModel
import com.example.everfitscannerapp.domain.model.ScanService
import com.example.everfitscannerapp.domain.usecase.GetScanDetailInfoUseCase
import com.example.everfitscannerapp.domain.usecase.SearchDetailUseCase
import com.example.everfitscannerapp.ui.BaseViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getScanDetailInfoUseCase: GetScanDetailInfoUseCase,
    private val searchDetailUseCase: SearchDetailUseCase,
) : BaseViewModel() {

    private var mode: Mode = Mode.SCAN
    private val scanServices: ArrayList<ScanService> = arrayListOf()

    fun initMode(services: List<ScanService>, mode: Mode, input: String) {
        scanServices.clear()
        scanServices.addAll(services)
        this.mode = mode
        getData(input)
    }

    fun getData(input: String) {
        if (mode == Mode.SCAN) {
            scanCode(input)
        } else {
            search(input)
        }
    }

    private val _result = MutableLiveData<List<ResultModel>>()
    val result : LiveData<List<ResultModel>> = _result


    private fun scanCode(barcodeStr: String) {
        val deferreds = hashMapOf<ScanService, Deferred<JsonObject?>>()
        viewModelScope.launch(Dispatchers.IO) {
            scanServices.filter { service -> service.isScanService() }.forEach { scanService ->
                deferreds[scanService] = async {
                    getScanDetailInfoUseCase.getScanDetailJs(
                        scanService.id,
                        scanCode = barcodeStr
                    )
                }
            }
            val scannedData : ArrayList<ResultModel> = arrayListOf()
            deferreds.forEach { data ->
                val resultData = arrayListOf<String>()
                data.value.await()?.let { jsData ->
                    resultData.add(jsData.toString())
                }
                scannedData.add(
                    ResultModel(
                        serviceName = data.key.name,
                        resultData = resultData
                    )
                )
            }
            _result.postValue(scannedData)
        }
    }

    private fun search(input: String) {
        val deferreds = hashMapOf<ScanService, Deferred<List<JsonObject>>>()
        viewModelScope.launch(Dispatchers.IO) {
            scanServices.filter { service -> service.isSearchService() }.forEach { scanService ->
                deferreds[scanService] = async {
                    searchDetailUseCase.getScanDetailJs(
                        scanService.id,
                        input = input
                    )
                }
            }
            val scannedData : ArrayList<ResultModel> = arrayListOf()
            deferreds.forEach { data ->
                val resultData = arrayListOf<String>()
                data.value.await().forEach { jsData ->
                    resultData.add(jsData.toString())
                }
                scannedData.add(
                    ResultModel(
                        serviceName = data.key.name,
                        resultData = resultData
                    )
                )
            }
            _result.postValue(scannedData)
        }
    }


}