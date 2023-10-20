package com.example.everfitscannerapp.data.repository

import com.example.everfitscannerapp.data.api.ScanServiceAPI
import com.example.everfitscannerapp.data.requests.ScanRequest
import com.example.everfitscannerapp.domain.model.ScanService
import com.example.everfitscannerapp.domain.model.ScannedData
import com.example.everfitscannerapp.domain.model.ScannedNutrition
import com.example.everfitscannerapp.domain.repository.ScanRepository
import com.google.gson.GsonBuilder
import java.lang.Exception

class ScanRepositoryImpl(private val scanServiceAPI: ScanServiceAPI) : ScanRepository {

    override suspend fun getScanService(): List<ScanService> {
        return try {
            scanServiceAPI.getService().data.list.map { serviceDetail ->
                ScanService(
                    id = serviceDetail.id,
                    name = serviceDetail.name
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun getScanDetail(serviceId: String, scanCode: String): ScannedData {
        val data = scanServiceAPI.getDetail(
            ScanRequest(
                id = serviceId,
                code = scanCode
            )
        ).data
        val dataNutrition = data.nutrition
        return try {
            ScannedData(
                name = data.name,
                nutrition = ScannedNutrition(
                    calories = dataNutrition.calories,
                    fat = dataNutrition.fat,
                    saturatedFat = dataNutrition.saturatedFat,
                    polyunsaturatedFat = dataNutrition.polyunsaturatedFat,
                    monounsaturatedFat = dataNutrition.monounsaturatedFat,
                    cholesterol = dataNutrition.cholesterol,
                    sodium = dataNutrition.sodium,
                    potassium = dataNutrition.potassium,
                    carbohydrates = dataNutrition.carbohydrates,
                    fiber = dataNutrition.fiber,
                    sugar = dataNutrition.sugar,
                    protein = dataNutrition.protein,
                    vitaminC = dataNutrition.vitaminC,
                    calcium = dataNutrition.calcium,
                    iron = dataNutrition.iron,
                    vitaminD = dataNutrition.vitaminD,
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ScannedData()
        }
    }

    override suspend fun getScanDetailJson(serviceId: String, scanCode: String): String {
        return try {
            val json = scanServiceAPI.getDetailJson(
                ScanRequest(
                    id = serviceId,
                    code = scanCode
                )
            )
            GsonBuilder().setPrettyPrinting().create().toJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            "${e.message}"
        }
    }
}