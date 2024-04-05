package com.backend.remindmedapi.controllers

import com.backend.remindmedapi.apiCalls.SingleDrugRes
import com.backend.remindmedapi.apiCalls.callListOfMedication
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import com.backend.remindmedapi.apiCalls.callOpenFdaSingleDrug
import com.google.gson.JsonObject

@RestController
@ComponentScan("com.backend.remindmedapi.services")
@ComponentScan("com.backend.remindmedapi.models")
@ComponentScan("com.backend.remindmedapi.apiCalls")
@RequestMapping("/medicine")
class MedicineController {
    @GetMapping("")
    @ResponseBody
    fun getMedicationById(@RequestParam("id") name: String): JsonObject? {
        val result: JsonObject = callOpenFdaSingleDrug(name)
        if (result.isJsonNull) {
            return null
        }
        return result
    }

    @GetMapping("/all")
    @ResponseBody
    fun getAllMedications(@RequestParam("name") name: String): MutableList<Pair<String, String>>{
        val result: MutableList<Pair<String, String>> = callListOfMedication(name)
        if (result.isEmpty()) {
            return mutableListOf()
        }
        return result
    }
}