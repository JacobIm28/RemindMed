package com.gradle.apiCalls

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


// loads in information related to a single drug
@OptIn(DelicateCoroutinesApi::class)
fun callOpenFdaSingleDrug(drug: String) {
    val client = HttpClient(Android)
    try {
        runBlocking{
            launch{
                val response: String = client.get("https://api.fda.gov/drug/label.json?search=openfda.brand_name:$drug&limit=1").bodyAsText()
                val data: SingleDrugRes = Gson().fromJson(response, SingleDrugRes::class.java)
                println(data)
            }
        }
    } catch(exception: Exception) {
        println(exception);
    }
}

// for our search bar later on. Ques up a whole list of drugs based on string input
// will use the rxnorm approximate match API
fun callListOfMedication(strSoFar: String) {
    val client = HttpClient(Android)
    try {
        runBlocking{
            launch{
                val response: String = client.get("https://rxnav.nlm.nih.gov/REST/approximateTerm.json?term=$strSoFar&maxEntries=100").bodyAsText()
                val data: rxNormData= Gson().fromJson(response, rxNormData::class.java)

                // will now begin looping through the candidates field and adding valid elements to our return array
                val res: MutableList<String> = mutableListOf("");
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
                println(res)
            }
        }
    } catch(exception: Exception) {
        println(exception);
    }
}
