package com.gradle.apiCalls

import com.gradle.models.Doctor
import com.gradle.models.Patient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

@OptIn(DelicateCoroutinesApi::class)
class DoctorApi {
    private val host: String = "https://remindmed-api-nsjyfltjaa-uk.a.run.app"
    private val nullDoctor = Doctor("-1", "", "")
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })

        }
    }

    fun getDoctor(id: String): Doctor {
        return try {
            var doctor: Doctor? = null
            runBlocking {
                launch {
                    doctor = client.get("$host/doctor?id=$id").body()
                }
            }
            if (doctor != null) {
                doctor as Doctor
            } else {
                nullDoctor
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun getDoctorbyEmail(email: String): Doctor {
        return try {
            var doctor: Doctor? = null
            runBlocking {
                launch {
                    doctor = client.get("$host/doctor/email?email=$email").body()
                }
            }
            if (doctor != null) {
                doctor as Doctor
            } else {
                nullDoctor
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun getAllDoctors(): ArrayList<Doctor> {
        return try {
            var doctors: ArrayList<Doctor>? = null
            runBlocking {
                launch {
                    doctors = client.get("$host/doctor/all").body()
                }
            }
            if (doctors != null) {
                doctors as ArrayList<Doctor>
            } else {
                ArrayList()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun addDoctor(doctor: Doctor): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    success = client.post("$host/doctor") {
                        contentType(ContentType.Application.Json)
                        setBody(doctor)
                    }.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteDoctor(id: String): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    success = client.delete("$host/doctor?id=$id").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun updateDoctor(doctor: Doctor): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    success =
                        client.put("$host/doctor?id=${doctor.did}&name=${doctor.name}&email=${doctor.email}").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun addPatient(did: String, pid: String): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    success =
                        client.post("$host/doctor/patient?did=$did&pid=$pid").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun removePatient(did: String, pid: String): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    success =
                        client.delete("$host/doctor/patient?did=$did&pid=$pid").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun getPatients(did: String): MutableList<Patient> {
        return try {
            var patients: MutableList<Patient>? = null
            runBlocking {
                launch {
                    patients = client.get("$host/doctor/patients?did=$did").body()
                }
            }
            if (patients?.isEmpty() == false) {
                patients as MutableList<Patient>
            } else {
                ArrayList()
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
