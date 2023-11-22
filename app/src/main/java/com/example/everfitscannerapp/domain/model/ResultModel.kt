package com.example.everfitscannerapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultModel(
    val serviceName: String,
    val resultData: List<String>
) : Parcelable