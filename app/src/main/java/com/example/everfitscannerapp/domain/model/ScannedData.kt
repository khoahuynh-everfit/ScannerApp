package com.example.everfitscannerapp.domain.model

data class ScannedData(
    val name : String = "",
    val nutrition: ScannedNutrition = ScannedNutrition()
)
