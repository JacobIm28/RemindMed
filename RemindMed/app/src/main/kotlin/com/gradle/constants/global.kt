package com.gradle.constants

import com.gradle.models.Doctor
import com.gradle.models.Patient

object GlobalObjects {
    var patient: Patient = Patient("-1", "", "")
    var doctor: Doctor = Doctor("-1", "", "")
    var type = ""
}
val doctorView = true
val CHANNEL_ID = "channel"
var notificationId = 1

var medicationInfoBlacklist: Set<String> = setOf<String>("spl_product_data_elements")
