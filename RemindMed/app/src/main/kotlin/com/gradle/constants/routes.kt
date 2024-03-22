package com.gradle.constants

object Routes {
    val HOME = "home"
    val MEDICATION_LIST = "medication-list"
    val PROFILE = "profile"
    val MEDICATION_ENTRY = "medication-entry"
    val PEOPLE_LIST = "people-list"
    val MEDICATION_INFO = "medication-info"
    val ADD_PATIENT = "add-patient"

    val MEDICATION_INFO_WITH_ARGS = MEDICATION_INFO + "?" +
            "${NavArguments.MEDICATION_INFO.MEDICATION_NAME}={${NavArguments.MEDICATION_INFO.MEDICATION_NAME}}&" +
            "${NavArguments.MEDICATION_INFO.START_DATE}={${NavArguments.MEDICATION_INFO.START_DATE}}&" +
            "${NavArguments.MEDICATION_INFO.END_DATE}={${NavArguments.MEDICATION_INFO.END_DATE}}&" +
            "${NavArguments.MEDICATION_INFO.DOSAGE}={${NavArguments.MEDICATION_INFO.DOSAGE}}"

    val MEDICATION_LIST_WITH_ARGS = MEDICATION_LIST + "?" +
            "${NavArguments.MEDICATION_LIST}={${NavArguments.MEDICATION_LIST.PID}}"
}

object NavArguments {
    object MEDICATION_INFO {
        val MEDICATION_NAME = "medicationName"
        val START_DATE = "startDate"
        val END_DATE = "endDate"
        val DOSAGE = "dosage"
    }

    object MEDICATION_LIST {
        val PID = "pid"
    }
}


