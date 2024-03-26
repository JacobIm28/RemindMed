package com.gradle.models

import kotlinx.serialization.Serializable

//TODO: Change once actual schema received from Jacob
@Serializable
/*
data class Patient (
    val pid: String,
    val name: String,
    val email: String
) {
    override fun toString(): String {
        return "Patient(pid=$pid, name='$name', email='$email')"
    }
}
 */
class Patient(var pid: String = "", var _name: String = "", var _email: String = "") : IPresenter() {
    var name: String = _name
        set(value) {
            field = value
            notifySubscribers()
        }

    var email: String = _email
        set(value) {
            field = value
            notifySubscribers()
        }

    var successfulChange = false
        set(value) {
            field = value
            notifySubscribers()
        }
    var errorMessage = ""
        set(value) {
            field = value
            notifySubscribers()
        }
    var isError = false
        set(value) {
            field = value
            notifySubscribers()
        }

    override fun toString(): String {
        return "Patient(pid=$pid, name='$name', email='$email')"
    }
}
