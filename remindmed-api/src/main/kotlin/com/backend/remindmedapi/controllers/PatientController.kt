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
import com.backend.remindmedapi.models.Patient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping

@RestController
@ComponentScan("com.backend.remindmedapi.services")
@ComponentScan("com.backend.remindmedapi.models")
@RequestMapping("/patient")
class PatientController {
    @Autowired
    lateinit var databaseService: DatabaseService
    @GetMapping("")
    @ResponseBody
    fun getPatient(@RequestParam("id") id: Int): Patient {
        val result: ArrayList<ArrayList<String>>? = databaseService.query("SELECT * FROM Patients p where p.pid = $id;")
        if(result.isNullOrEmpty()) {
            return Patient(-1, "No patient found", "No patient found")
        }
        return Patient(result[0][0].toInt(), result[0][1], result[0][2])
    }

    @GetMapping("/all")
    @ResponseBody
    //WARNING: This is a very dangerous method, as it returns all patients in the database
    //TODO: Remove this method once we have a proper authentication system in place
    fun getAllPatients(): ArrayList<Patient> {
        val result = databaseService.query("SELECT * FROM Patients;")
        val response = ArrayList<Patient>()
        for (patient in result!!) {
            response.add(Patient(patient[0].toInt(), patient[1], patient[2]))
        }
        return response
    }

    @PostMapping("/add")
    @ResponseBody
    fun addPatient(@RequestParam("name") name: String, @RequestParam("email") email: String): String {
        val result = databaseService.query("INSERT INTO Patients (name, email) VALUES ('$name', '$email') RETURNING pid;")
        return "Inserted Patient with name: $name and email: $email with new id: ${result!![0][0]}"
    }

    @PostMapping("/delete")
    @ResponseBody
    //Option to delete is currently restricted to id, in case of dupe emails/names
    fun deletePatient(@RequestParam("id") id: Int): String {
        val result = databaseService.query("DELETE FROM Patients WHERE pid = $id RETURNING pid;")
        return "Deleted Patient with id: $id"
    }

    @PutMapping("/update")
    @ResponseBody
    fun updatePatient(@RequestParam("id") id: Int, @RequestParam("name", required = false, defaultValue = "") name: String,
                     @RequestParam("email", required = false, defaultValue = "") email: String): String {
        var queryStr = "UPDATE Patients SET "
        if (name != "") {
            queryStr += "name = '$name', "

            if (email != "") {
                queryStr += "email = '$email'"
            }
        } else if (email != "") {
            queryStr += "email = '$email'"
        }
        queryStr += " WHERE pid = $id RETURNING pid;"
        val result = databaseService.query(queryStr)
        return "Updated Patient with id: $id"
    }

    @PostMapping("/medicine")
    @ResponseBody
    fun addMedicine(@RequestParam("pid") pid: Int, @RequestParam("mid") mid: Int, @RequestParam("amount") amount: String): String {
        val result = databaseService.query("INSERT INTO Medication (pid, mid, amount) VALUES ($pid, $mid, $amount) RETURNING pid;")
        return "Inserted Medicine with id: $mid to Patient with id: $pid"
    }

    @DeleteMapping("/medicine")
    @ResponseBody
    fun removeMedicine(@RequestParam("pid") pid: Int, @RequestParam("mid") mid: Int): String {
        val result = databaseService.query("DELETE FROM Medication WHERE pid = $pid AND mid = $mid RETURNING pid;")
        return "Removed Medicine with id: $mid from Patient with id: $pid"
    }

    @GetMapping("/medicines")
    @ResponseBody
    fun getMedicines(@RequestParam("pid") pid: Int): ArrayList<ArrayList<Int>>? {
        val result = databaseService.query("SELECT m.mid FROM Medication m WHERE pm.pid = $pid;")
        val response = ArrayList<ArrayList<Int>>()
        for (medicine in result!!) {
            response.add(arrayListOf(medicine[0].toInt()))
        }
        return response
    }
}