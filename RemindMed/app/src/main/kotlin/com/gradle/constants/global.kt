package com.gradle.constants

import com.gradle.models.Doctor
import com.gradle.models.Patient

object GlobalObjects {
    var patient: Patient = Patient("-1", "", "")
    var doctor: Doctor = Doctor("-1", "", "")
    var type = ""
}