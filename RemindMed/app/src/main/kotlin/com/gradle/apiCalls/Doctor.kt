package com.gradle.apiCalls

import com.gradle.models.Doctor
import com.gradle.models.Patient
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json

@OptIn(DelicateCoroutinesApi::class)
class Doctor {
    //TODO: Change host to server's address once API deployed to some server
    private val host: String = "http://10.0.2.2:8080"
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
                    println("Getting doctor with id: $id")
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
                    println("Getting doctor with email: $email")
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

    //TODO: Kind of useless, remove if not needed. Good for testing, though
    fun getAllDoctors(): ArrayList<Doctor> {
        return try {
            var doctors: ArrayList<Doctor>? = null
            runBlocking {
                launch {
                    println("Getting all doctors")
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
                    println("Adding doctor")
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
                    println("Deleting doctor with id: $id")
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
                    println("Updating doctor: $doctor")
                    success = client.put("$host/doctor?id=${doctor.did}&name=${doctor.name}&email=${doctor.email}").status.isSuccess()
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
                    println("Adding patient with id: $pid to doctor with id: $did")
                    success = client.post("$host/doctor/patient?did=$did&pid=$pid").status.isSuccess()
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
                    println("Removing patient with id: $pid from doctor with id: $did")
                    success = client.delete("$host/doctor/patient?did=$did&pid=$pid").status.isSuccess()
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
                    println("Getting all patients for doctor with id: $did")
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
