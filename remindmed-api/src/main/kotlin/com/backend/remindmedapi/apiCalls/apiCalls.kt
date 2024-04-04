package com.backend.remindmedapi.apiCalls

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*

fun MutableList<String>.contains(s: String, ignoreCase: Boolean = false): Boolean {

    return any { it.equals(s, ignoreCase) }
}


// loads in information related to a single drug
@OptIn(DelicateCoroutinesApi::class)
fun callOpenFdaSingleDrug(drug: String): JsonObject {
    val client = HttpClient(Android)
    try {
        var data: JsonObject = JsonObject()
        var res: String? = null
        runBlocking{
            launch{
                val response: String = client.get("https://api.fda.gov/drug/label.json?search=$drug&limit=1").bodyAsText()
                // cast the response to a JsonObject of type SingleDrugRes
                data = JsonParser.parseString(response).asJsonObject

            }
        }
        return data
    } catch(exception: Exception) {
        println(exception);
        return JsonObject()
    }
}

fun callListOfMedication(strSoFar: String): MutableList<String> {
    val client = HttpClient(Android)
    try {
        val res = mutableListOf<String>()
        runBlocking{
            launch{
                val response: String = client.get("https://api.fda.gov/drug/ndc.json?search=brand_name:$strSoFar*&limit=20").body()
                val parsedJson = JsonParser.parseString(response).asJsonObject
                val results = parsedJson["results"].asJsonArray
                for (result in results) {
                    val drug = result.asJsonObject["brand_name"].asString
                    if (!res.contains(drug, true)) {
                        res.add(drug)
                    }
                }
            }
        }
        return res
    } catch(exception: Exception) {
        return mutableListOf()
    }
}
