package com.backend.remindmedapi.controllers

import com.backend.remindmedapi.models.Doctor
import com.backend.remindmedapi.models.Medication
import com.backend.remindmedapi.services.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import com.backend.remindmedapi.models.Patient
import com.google.gson.stream.JsonReader
import com.google.gson.JsonObject
import org.postgresql.util.PGobject
import org.springframework.web.bind.annotation.*
import java.sql.Date
import java.sql.Time
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@RestController
@ComponentScan("com.backend.remindmedapi.services")
@ComponentScan("com.backend.remindmedapi.models")
@RequestMapping("/patient")
class PatientController {
    @Autowired
    lateinit var databaseService: DatabaseService
    @GetMapping("")
    @ResponseBody
    fun getPatientById(@RequestParam("id") id: String): Patient {
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Patients p where p.pid = '$id';")
        if(result.isNullOrEmpty()) {
            return Patient("-1", "No patient found", "No patient found")
        } else if(result[0][0] is String && result[0][1] is String && result[0][2] is String) {
            return Patient(result[0][0] as String, result[0][1] as String, result[0][2] as String)
        } else {
            return Patient("-1", "No patient found", "No patient found")
        }
    }

    @GetMapping("/email")
    @ResponseBody
    fun getPatientByEmail(@RequestParam("email") email: String): Patient {
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Patients p where p.email = '$email';")
        if(result.isNullOrEmpty()) {
            return Patient("-1", "No patient found", "No patient found")
        } else if(result[0][0] is String && result[0][1] is String && result[0][2] is String) {
            return Patient(result[0][0] as String, result[0][1] as String, result[0][2] as String)
        } else {
            return Patient("-1", "No patient found", "No patient found")
        }
    }

    @GetMapping("/doctors")
    @ResponseBody
    fun getDoctors(@RequestParam("pid") pid: String): MutableList<Doctor>? {
        val result = databaseService.query("SELECT d.* FROM Doctors d JOIN treatment t ON d.did = t.did WHERE t.pid = '$pid';")
        val response = mutableListOf<Doctor>()
        for (doctor in result!!) {
            if(doctor[0] is String && doctor[1] is String && doctor[2] is String) {
                response.add(Doctor(doctor[0] as String, doctor[1] as String, doctor[2] as String))
            }
        }
        return response
    }

    @GetMapping("/all")
    @ResponseBody
    //WARNING: This is a very dangerous method, as it returns all patients in the database
    //TODO: Remove this method once we have a proper authentication system in place
    fun getAllPatients(): MutableList<Patient> {
        val result = databaseService.query("SELECT * FROM Patients;")
        val response = mutableListOf<Patient>()
        for (patient in result!!) {
            if(patient[0] is String && patient[1] is String && patient[2] is String) {
                response.add(Patient(patient[0] as String, patient[1] as String, patient[2] as String))
            }
        }
        return response
    }

    @PostMapping("/add")
    @ResponseBody
    fun addPatient(@RequestBody patient: Patient): String {
       val result = databaseService.query("INSERT INTO Patients (pid, name, email) VALUES ('${patient.pid}', '${patient.name}', '${patient.email}') RETURNING pid;")
        return "Inserted Patient with name: ${patient.name} and email: ${patient.email} with new id: ${result!![0][0]}"
    }

    @DeleteMapping("/delete")
    @ResponseBody
    //Option to delete is currently restricted to id, in case of dupe emails/names
    fun deletePatient(@RequestParam("id") id: String): String {
        databaseService.query("DELETE FROM Patients WHERE pid = $id RETURNING pid;")
        return "Deleted Patient with id: $id"
    }

    @PutMapping("/update")
    @ResponseBody
    fun updatePatient(@RequestParam("id") id: String, @RequestParam("name", required = false, defaultValue = "") name: String,
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
        queryStr += " WHERE pid = '$id' RETURNING pid;"
        databaseService.query(queryStr)
        return "Updated Patient with id: $id"
    }

    @PostMapping("/medicine")
    @ResponseBody
    fun addMedicine(@RequestBody medication: Medication): String {
        println(medication)
        val timeString = "[" + medication.times.joinToString(",") { "to_timestamp(\'${it}\', 'hh24:mi:ss')"} + "]"
        val queryString = "INSERT INTO Medication (pid, medication_id, amount, startDate, endDate, name, notes, times, accepted, taken) VALUES ('${medication.pid.toString().replace("\"", "")}', '${medication.medicationId.toString().replace("\"", "")}', '${medication.amount.toString().replace("\"", "")}', " +
                "'${medication.startDate.toString().replace("\"", "")}', '${medication.endDate.toString().replace("\"", "")}', '${medication.name.replace("\"", "")}', '${medication.notes.replace("\"", "")}', ARRAY $timeString, ${medication.accepted}, ${medication.taken}) RETURNING pid;"
        databaseService.query(queryString)
        return "Inserted Medicine with id: ${medication.medicationId} Patient with id: ${medication.pid}"
    }

    @DeleteMapping("/medicine")
    @ResponseBody
    fun removeMedicine(@RequestParam("pid") pid: String, @RequestParam("mid") mid: String): String {
        databaseService.query("DELETE FROM Medication WHERE pid = '$pid' AND medication_id = '$mid' RETURNING pid;")
        return "Removed Medicine with id: $mid from Patient with id: $pid"
    }

    @PutMapping("/medicine")
    @ResponseBody
    fun updateMedicine(@RequestBody medication: Medication): String {
        var queryStr = "UPDATE Medication SET "
        if (medication.amount != "") {
            queryStr += "amount = '${medication.amount}', "
        }
        if (medication.startDate != Date(0)) {
            queryStr += "startDate = '${medication.startDate}', "
        }
        if (medication.endDate != Date(0)) {
            queryStr += "endDate = '${medication.endDate}', "
        }
        if (medication.name != "") {
            queryStr += "name = '${medication.name}', "
        }
        if (medication.notes != "") {
            queryStr += "notes = '${medication.notes}', "
        }
        if (medication.times.isNotEmpty()) {
            val timeString = "[" + medication.times.joinToString(",") { "to_timestamp(\'${it}\', 'hh24:mi:ss')"} + "]"
            queryStr += "times = ARRAY ${timeString}, "
        }
        if (medication.accepted.toString() != "") {
            queryStr += "accepted = ${medication.accepted}, "
        }
        if (medication.taken.toString() != "") {
            queryStr += "taken = ${medication.taken}, "
        }
        queryStr = queryStr.dropLast(2)
        queryStr += " WHERE pid = '${medication.pid}' AND medication_id = '${medication.medicationId}' RETURNING pid;"
        databaseService.query(queryStr)
        return "Updated Medicine with id: ${medication.medicationId} for Patient with id: ${medication.pid}"
    }

    @GetMapping("/medicines")
    @ResponseBody
    fun getMedicines(@RequestParam("pid") pid: String): MutableList<Medication>? {
        val result = databaseService.query("SELECT m.* FROM Medication m WHERE m.pid = '$pid';")
        val response = mutableListOf<Medication>()
        for (medicine in result!!) {
            if(medicine[0] is String && medicine[1] is String && medicine[2] is String && medicine[3] is Date && medicine[4] is Date && medicine[5] is String && medicine[6] is String && medicine[7] is Array<*> && (medicine[7] as Array<*>).isArrayOf<Time>() && medicine[8] is Boolean && medicine[9] is Boolean) {
                val times = mutableListOf<Time>()
                for (time in medicine[7] as Array<*>) {
                    times.add(time as Time)
                }
                response.add((Medication(medicine[0] as String, medicine[1] as String, medicine[2] as String, medicine[3] as Date, medicine[4] as Date, medicine[5] as String, medicine[6] as String, times, medicine[8] as Boolean, medicine[9] as Boolean)))
            }
        }
        return response
    }
}
