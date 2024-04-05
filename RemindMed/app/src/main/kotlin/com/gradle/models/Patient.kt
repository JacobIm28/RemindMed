package com.gradle.models

import kotlinx.serialization.Serializable
@Serializable
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

    var submitEnabled = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var changesSubmitted = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var logoutClicked = false
        set(value) {
            field = value
            notifySubscribers()
        }

    override fun toString(): String {
        return "Patient(pid=$pid, name='$name', email='$email')"
    }
}
