package com.backend.remindmedapi.models
//TODO: Change once actual schema received from Jacob
data class Patient (
    val pid: Int,
    val name: String,
    val email: String
) {
    override fun toString(): String {
        return "Patient(pid=$pid, name='$name', email='$email')"
    }
}