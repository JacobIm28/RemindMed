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
class MedicationApi {
    private val host: String = "https://remindmed-api-nsjyfltjaa-uk.a.run.app"
    private val nullMedication = Medication("-1", "", "", Date(0), Date(0), "", "", mutableListOf<Time>(), false, false)
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })

        }
    }
    fun getMedicationbyId(id: String): JsonObject {
        return try {
            var medication: JsonObject? = null
            runBlocking {
                launch {
                    println("Getting medication with id: $id")
                    val response = client.get("$host/medicine?id=$id").bodyAsText()
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
    // First is name, second is med id
    fun getAllMedicationsbyName(name: String): MutableList<Pair<String, String>> {
        return try {
            var medications: MutableList<Pair<String, String>>? = null
            runBlocking {
                launch {
                    println("Getting all medications with name: $name")
                    medications = client.get("$host/medicine/all?name=$name").body()
                }
            }
            if (medications != null) {
                medications as MutableList<Pair<String, String>>
            } else {
                mutableListOf()
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
