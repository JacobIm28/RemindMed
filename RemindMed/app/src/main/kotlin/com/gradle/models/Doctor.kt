package com.gradle.models
import kotlinx.serialization.Serializable

//TODO: Change once actual schema received from Jacob
@Serializable
data class Doctor(
    val did: String,
    val name: String,
    val email: String
) {
    override fun toString(): String {
        return "Doctor(did=$did, name='$name', email='$email')"
    }
}
