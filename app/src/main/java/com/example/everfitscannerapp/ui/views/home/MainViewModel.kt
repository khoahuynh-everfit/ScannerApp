package com.example.everfitscannerapp.ui.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.everfitscannerapp.domain.model.ScanService
import com.example.everfitscannerapp.domain.usecase.GetServiceUseCase
import com.example.everfitscannerapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getServiceUseCase: GetServiceUseCase
) : BaseViewModel() {

    private val _listService: MutableLiveData<List<ScanService>> = MutableLiveData<List<ScanService>>()
    val listService: LiveData<List<ScanService>> = _listService

    fun getServices() {
        viewModelScope.launch(Dispatchers.IO) {
            val listServices = getServiceUseCase.getService()
            viewModelScope.launch(Dispatchers.Main) {
                _listService.value = listServices
            }
        }
    }

    fun getScanServices() : List<ScanService> {
        return listService.value?.filter { s -> s.isScanService() } ?: arrayListOf()
    }

    fun getSearchServices() : List<ScanService> {
        return listService.value?.filter { s -> s.isSearchService() } ?: arrayListOf()
    }
}