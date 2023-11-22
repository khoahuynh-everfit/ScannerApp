package com.example.everfitscannerapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanService(
    val id: String = "",
    val name: String = "",
    var services: List<String> = listOf()
) : Parcelable {

    fun isScanService(): Boolean = services.contains("look-up")

    fun isSearchService(): Boolean = services.contains("search")

}
