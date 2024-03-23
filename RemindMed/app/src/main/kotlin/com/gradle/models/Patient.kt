package com.gradle.models

import kotlinx.serialization.Serializable

//TODO: Change once actual schema received from Jacob
@Serializable
data class Patient (
    val pid: String,
    val name: String,
    val email: String
) {
    override fun toString(): String {
        return "Patient(pid=$pid, name='$name', email='$email')"
    }
}
