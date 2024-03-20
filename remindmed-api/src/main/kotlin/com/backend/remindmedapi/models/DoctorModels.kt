package com.backend.remindmedapi.models

data class Doctor(
    val did: String,
    val name: String,
    val email: String
) {
    override fun toString(): String {
        return "Doctor(did=$did, name='$name', email='$email')"
    }
}
