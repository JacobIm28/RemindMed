package com.gradle.apiCalls

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.gradle.models.Medication
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.sql.Date
import java.sql.Time

@OptIn(DelicateCoroutinesApi::class)
class Medication {
    //TODO: Change host to server's address once API deployed to some server
    private val host: String = "http://10.0.2.2:8080"
    private val nullMedication = Medication("-1", "", "", Date(0), Date(0), "", "", mutableListOf<Time>())
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })

        }
    }
    fun getMedicationbyName(name: String): JsonObject {
        return try {
            var medication: JsonObject? = null
            runBlocking {
                launch {
                    println("Getting medication with name: $name")
                    val response = client.get("$host/medicine?name=$name").bodyAsText()
                    medication = JsonParser.parseString(response).asJsonObject
                }
            }
            if (medication != null) {
                medication as JsonObject
            } else {
                JsonObject()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    // Use for searching medicines by name, only returns name of medications
    fun getAllMedicationsbyName(name: String): MutableList<String> {
        return try {
            var medications: MutableList<String>? = null
            runBlocking {
                launch {
                    println("Getting all medications with name: $name")
                    medications = client.get("$host/medicine/all?name=$name").body()
                }
            }
            if (medications != null) {
                medications as MutableList<String>
            } else {
                mutableListOf()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun getPatientMedications(pid: Int): ArrayList<ArrayList<Medication>> {
        return try {
            var medications: ArrayList<ArrayList<Medication>>? = null
            runBlocking {
                launch {
                    println("Getting all medications for patient with id: $pid")
                    medications = client.get("$host/medicine/patient?pid=$pid").body()
                }
            }
            if (medications != null) {
                medications as ArrayList<ArrayList<Medication>>
            } else {
                ArrayList()
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
