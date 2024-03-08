package com.backend.remindmedapi.apiCalls

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*


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

// for our search bar later on. Ques up a whole list of drugs based on string input
// will use the rxnorm approximate match API
fun callListOfMedication(strSoFar: String): MutableList<String> {
    val client = HttpClient(Android)
    try {
        val res = mutableListOf<String>()
        runBlocking{
            launch{
                val response: String = client.get("https://rxnav.nlm.nih.gov/REST/approximateTerm.json?term=$strSoFar&maxEntries=10000").bodyAsText()
                val data: RxNormData= Gson().fromJson(response, RxNormData::class.java)

                // will now begin looping through the candidates field and adding valid elements to our return array
                val numReturn : Int = 10;
                var numSoFar : Int = 0
                for (candidate in data.approximateGroup.candidate) {
                    if (numSoFar == numReturn){
                        break;
                    }
                    if (candidate.name != null) {
                        res.add(candidate.name)
                        numSoFar += 1;
                    }
                }
                res.removeFirst()
            }
        }
        return res
    } catch(exception: Exception) {
        return mutableListOf()
    }
}
