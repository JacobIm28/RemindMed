package com.backend.remindmedapi.controllers

import com.backend.remindmedapi.services.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import com.backend.remindmedapi.models.Doctor
import com.backend.remindmedapi.models.Patient
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

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
        val result: ArrayList<ArrayList<String>>? = databaseService.query("SELECT * FROM Doctors d where d.did = $id;")
        if (result.isNullOrEmpty()) {
            return Doctor(-1, "No doctor found", "No doctor found")
        }
        return Doctor(result[0][0].toInt(), result[0][1], result[0][2])
    }

    @GetMapping("/all")
    @ResponseBody
    fun getAllDoctors(): ArrayList<Doctor>? {
        val result = databaseService.query("SELECT * FROM Doctors;")
        val response = ArrayList<Doctor>()
        for (doctor in result!!) {
            response.add(Doctor(doctor[0].toInt(), doctor[1], doctor[2]))
        }
        return response
    }

    @PostMapping("/add")
    @ResponseBody
    fun addDoctor(@RequestBody doctor: Doctor): String {
        val result = databaseService.query("INSERT INTO Doctors (name, email) VALUES ('${doctor.name}', '${doctor.email}') RETURNING did;")
        return "Inserted Doctor with name: ${doctor.name} and email: ${doctor.email} with new id: ${result!![0][0]}"
    }

    @PostMapping("/delete")
    @ResponseBody
    //Option to delete is currently restricted to id, in case of dupe emails/names
    fun deleteDoctor(@RequestParam("id") id: Int): String {
        val result = databaseService.query("DELETE FROM Doctors WHERE did = $id RETURNING did;")
        return "Deleted Doctor with id: $id"
    }

    @PutMapping("/update")
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

    @PostMapping("/removePatient")
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
            response.add(arrayListOf(Patient(patient[0].toInt(), patient[1], patient[2])))
        }

        return response
    }
}