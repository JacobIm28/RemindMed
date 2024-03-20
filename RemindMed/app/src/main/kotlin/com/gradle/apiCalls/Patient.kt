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
    //TODO: Change host to server's address once API deployed to some server
    private val host: String = "http://10.0.2.2:8080"
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

    fun addMedication(pid: String, mid: String, amount: String): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Adding medication with id: $mid to patient with id: $pid")
                    success = client.post("$host/patient/medicine?pid=$pid&mid=$mid&amount=$amount").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

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

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMedicines(pid: String): MutableList<Medication> {
        return try {
            var medicines: MutableList<Medication>? = null
            runBlocking {
                launch {
                    val meds = JsonParser.parseString(client.get("$host/patient/medicines?pid=$pid").bodyAsText()).asJsonArray
                    medicines = mutableListOf()
                    meds.forEach {
                        val med = it.asJsonObject
                        val parsedDate = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(med["startDate"].asString)
                        val startDate = Date.valueOf(SimpleDateFormat("yyyy-MM-dd").format(parsedDate!!))
                        val parsedDate2 = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(med["endDate"].asString)
                        val endDate = Date.valueOf(SimpleDateFormat("yyyy-MM-dd").format(parsedDate2!!))
                        val times = mutableListOf<Time>()
                        med["times"].asJsonArray.forEach {
                            val parsedTime = SimpleDateFormat("hh:mm:ss", Locale.ENGLISH).parse(it.asString)
                            val time = Time.valueOf(SimpleDateFormat("hh:mm:ss").format(parsedTime!!))
                            times.add(time)
                        }
                        medicines?.add(Medication(
                            med["pid"].asString,
                            med["medicationId"].asString,
                            med["amount"].asString,
                            startDate,
                            endDate,
                            med["name"].asString,
                            med["notes"].asString,
                            times
                        ))
                    }
                }
            }
            if (medicines?.isEmpty() == false) {
                medicines!!
            } else {
                mutableListOf<Medication>()
            }
        } catch (e: Exception) {
            throw e
        }
    }
}