package com.example.everfitscannerapp.data.responses

import com.google.gson.annotations.SerializedName

data class ScanDataResponse(
    @SerializedName("data") val data: ScanDataDetailResponse
)

data class ScanDataDetailResponse(
    @SerializedName("name") val name: String,
    @SerializedName("nutrition") val nutrition: ScanDataNutritionResponse
)

data class ScanDataNutritionResponse(
    @SerializedName("calories") val calories: Double,
    @SerializedName("fat") val fat: Double,
    @SerializedName("saturated_fat") val saturatedFat: Double,
    @SerializedName("polyunsaturated_fat") val polyunsaturatedFat: Double,
    @SerializedName("monounsaturated_fat") val monounsaturatedFat: Double,
    @SerializedName("cholesterol") val cholesterol: Double,
    @SerializedName("sodium") val sodium: Double,
    @SerializedName("potassium") val potassium: Double,
    @SerializedName("carbohydrates") val carbohydrates: Double,
    @SerializedName("fiber") val fiber: Double,
    @SerializedName("sugar") val sugar: Double,
    @SerializedName("protein") val protein: Double,
    @SerializedName("vitamin_c") val vitaminC: Double,
    @SerializedName("calcium") val calcium: Double,
    @SerializedName("iron") val iron: Double,
    @SerializedName("vitamin_d") val vitaminD: Double,
)