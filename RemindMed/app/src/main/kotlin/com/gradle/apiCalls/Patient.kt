package com.gradle.apiCalls

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.gradle.models.Medication
import com.gradle.models.Patient
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json
import com.google.gson.JsonParser
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.gradle.models.Doctor

@OptIn(DelicateCoroutinesApi::class)
class Patient {

    private val host: String = "https://remindmed-api-nsjyfltjaa-uk.a.run.app"
    private val nullPatient = Patient("-1", "", "")
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            ContentType.Application.Json
        }
    }
    fun getPatientbyId(id: String): Patient {
        return try {
            var patient: Patient? = null
            runBlocking {
                launch {
                    println("Getting patient with id: $id")
                    patient = client.get("$host/patient?id=$id").body()
                }
            }
            if (patient != null) {
                patient as Patient
            } else {
                nullPatient
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun getPatientbyEmail(email: String): Patient {
        return try {
            var patient: Patient? = null
            runBlocking {
                launch {
                    println("Getting patient with email: $email")
                    patient = client.get("$host/patient/email?email=$email").body()
                }
            }
            if (patient != null) {
                patient as Patient
            } else {
                nullPatient
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun getDoctors(pid: String): MutableList<Doctor> {
        return try {
            var doctors: MutableList<Doctor>? = null
            runBlocking {
                launch {
                    println("Getting all doctors")
                    val res = client.get("$host/patient/doctors?pid=$pid").bodyAsText()
                    doctors = JsonParser.parseString(res).asJsonArray.map {
                        val doc = it.asJsonObject
                        Doctor(doc["did"].asString, doc["name"].asString, doc["email"].asString)
                    }.toMutableList()
                }
            }
            if (doctors?.isEmpty() == false) {
                doctors as MutableList<Doctor>
            } else {
                mutableListOf<Doctor>()
            }
        } catch (e: Exception) {
            throw e
        }
    }


    // TODO: Kind of useless, remove if not needed. Good for testing, though
    fun getAllPatients(): MutableList<Patient> {
        return try {
            var patients: MutableList<Patient>? = null
            runBlocking {
                launch {
                    println("Getting all patients")
                    patients = client.get("$host/patient/all").body()
                }
            }
            if (patients?.isEmpty() == false) {
                patients as MutableList<Patient>
            } else {
                mutableListOf<Patient>()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    @OptIn(InternalAPI::class)
    fun addPatient(patient: Patient): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Adding patient: $patient")
                    success = client.post("$host/patient/add") {
                        contentType(ContentType.Application.Json)
                        setBody(patient)
                    }.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun deletePatient(id: String): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Deleting patient with id: $id")
                    success = client.delete("$host/patient/delete?id=$id").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    // TESTED all endpoints above, will not test anymore from here.
    // Please contact Samir Ali if any issues arise
    fun updatePatient(patient: Patient): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Updating patient: $patient")
                    success = client.put("$host/patient/update?id=${patient.pid}&name=${patient.name}&email=${patient.email}").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun addMedication(medication: Medication): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Adding medication: ${medication}")
                    println("${medication}")
                    success = client.post("$host/patient/medicine"){
                        contentType(ContentType.Application.Json)
                        setBody(medication)
                    }.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

//    suspend fun removeMedication(pid: String, mid: String): Boolean {
//        return try {
//            println("Removing medication with id: $mid from patient with id: $pid")
//            val response = client.delete("$host/patient/medicine?pid=$pid&mid=$mid").status.isSuccess()
//            response
//        } catch (e: Exception) {
//            false
//        }
//    }


     fun removeMedication(pid: String, mid: String): Boolean {
         return try {
             var success = false
             runBlocking {
                 launch {
                     println("Removing medication with id: $mid from patient with id: $pid")
                     success = client.delete("$host/patient/medicine?pid=$pid&mid=$mid").status.isSuccess()
                 }
             }
             success
         } catch (e: Exception) {
             throw e
         }
     }

    fun updateMedication(medication: Medication): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Updating medication: $medication")
                    success = client.put("$host/patient/medicine"){
                        contentType(ContentType.Application.Json)
                        setBody(medication)
                    }.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMedicines(pid: String): MutableList<Medication> {
        return try {
            var meds = mutableListOf<Medication>()
            runBlocking {
                launch {
                    meds = client.get("$host/patient/medicines?pid=$pid").body<MutableList<Medication>>()

                }
            }
            meds.ifEmpty {
                mutableListOf<Medication>()
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
