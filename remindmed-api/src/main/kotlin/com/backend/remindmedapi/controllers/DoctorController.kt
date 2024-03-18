package com.backend.remindmedapi.controllers

import com.backend.remindmedapi.services.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import com.backend.remindmedapi.models.Doctor
import com.backend.remindmedapi.models.Patient
import org.springframework.web.bind.annotation.*

@RestController
@ComponentScan("com.backend.remindmedapi.services")
@ComponentScan("com.backend.remindmedapi.models")
@RequestMapping("/doctor")
class DoctorController {
    @Autowired
    lateinit var databaseService: DatabaseService
    @GetMapping("")
    @ResponseBody
    fun getDoctor(@RequestParam("id") id: Int): Doctor {
        val result: ArrayList<ArrayList<Any>>? = databaseService.query("SELECT * FROM Doctors d where d.did = $id;")
        if (result.isNullOrEmpty()) {
            return Doctor(-1, "No doctor found", "No doctor found")
        } else if (result[0][0] is Int && result[0][1] is String && result[0][2] is String) {
            return Doctor(result[0][0] as Int, result[0][1] as String, result[0][2] as String)
        } else {
            return Doctor(-1, "No doctor found", "No doctor found")
        }
    }

    @GetMapping("/all")
    @ResponseBody
    fun getAllDoctors(): ArrayList<Doctor>? {
        val result = databaseService.query("SELECT * FROM Doctors;")
        val response = ArrayList<Doctor>()
        for (doctor in result!!) {
            if(doctor[0] is Int && doctor[1] is String && doctor[2] is String) {
                response.add(Doctor(doctor[0] as Int, doctor[1] as String, doctor[2] as String))
            }
        }
        return response
    }

    @PostMapping("")
    @ResponseBody
    fun addDoctor(@RequestBody doctor: Doctor): String {
        val result = databaseService.query("INSERT INTO Doctors (name, email) VALUES ('${doctor.name}', '${doctor.email}') RETURNING did;")
        return "Inserted Doctor with name: ${doctor.name} and email: ${doctor.email} with new id: ${result!![0][0]}"
    }

    @DeleteMapping("")
    @ResponseBody
    //Option to delete is currently restricted to id, in case of dupe emails/names
    fun deleteDoctor(@RequestParam("id") id: Int): String {
        val result = databaseService.query("DELETE FROM Doctors WHERE did = $id RETURNING did;")
        return "Deleted Doctor with id: $id"
    }

    @PutMapping("")
    @ResponseBody
    fun updateDoctor(@RequestParam("id") id: Int, @RequestParam("name", required = false, defaultValue = "") name: String,
                     @RequestParam("email", required = false, defaultValue = "") email: String): String {
        var queryStr = "UPDATE Doctors SET "
        if (name != "") {
            queryStr += "name = '$name'"

            if(email != "") {
                queryStr += ", email = '$email'"
            }
        } else if (email != "") {
            queryStr += "email = '$email'"
        }
        queryStr += " WHERE did = $id RETURNING did;"
        val result = databaseService.query(queryStr)
        return "Updated Doctor with id: $id"
    }

    @PostMapping("/patient")
    @ResponseBody
    fun addPatient(@RequestParam("did") did: Int, @RequestParam("pid") pid: Int): String {
        val result = databaseService.query("INSERT INTO Treatment (did, pid) VALUES ($did, $pid) RETURNING did;")
        return "Inserted Patient with id: $pid to Doctor with id: $did"
    }

    @DeleteMapping("/patient")
    @ResponseBody
    fun removePatient(@RequestParam("did") did: Int, @RequestParam("pid") pid: Int): String {
        val result = databaseService.query("DELETE FROM Treatment WHERE did = $did AND pid = $pid RETURNING did;")
        return "Removed Patient with id: $pid from Doctor with id: $did"
    }

    @GetMapping("/patients")
    @ResponseBody
    fun getPatients(@RequestParam("did") did: Int): ArrayList<ArrayList<Patient>> {
        val result = databaseService.query("SELECT * FROM Patients p WHERE p.pid IN (SELECT t.pid FROM Treatment t WHERE t.did = $did);")
        val response = ArrayList<ArrayList<Patient>>()
        for (patient in result!!) {
          if(patient[0] is Int && patient[1] is String && patient[2] is String) {
              response.add(arrayListOf(Patient(patient[0] as Int, patient[1] as String, patient[2] as String)))
          }
        }

        return response
    }
}