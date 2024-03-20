package com.gradle.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.callback.Callback
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.result.Credentials
import android.util.Log
import com.auth0.android.jwt.JWT
import com.example.remindmed.R
import com.gradle.constants.GlobalObjects
import com.gradle.apiCalls.Patient as PatientApi
import com.gradle.apiCalls.Doctor as DoctorApi


class LoginModel: ViewModel() {

    var appJustLaunched by mutableStateOf(true)
    var userIsAuthenticated by mutableStateOf(false)
    var userIsComplete by mutableStateOf(false)

    private val TAG = "MainViewModel"  // 1
    private var account: Auth0 = Auth0(R.string.com_auth0_client_id.toString(), R.string.com_auth0_domain.toString())  // 2
    private lateinit var context: Context  // 3

    var user by mutableStateOf(User())

    fun login() {
        WebAuthProvider
            .login(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    Log.e(TAG, "Error occurred in login(): $error")
                }

                override fun onSuccess(result: Credentials) {
                    val idToken = result.idToken

                    var jwt = JWT(idToken ?: "").subject ?: "1"
                    if(jwt.startsWith("auth0|")) {
                        jwt = jwt.slice(6 until jwt.length)
                    }
                    println(jwt)
                    val patient: Patient = PatientApi().getPatientbyId(jwt)
                    val doctor: Doctor = DoctorApi().getDoctor(jwt)

                    if(patient.pid != "-1") {
                        user = User(patient.pid, patient.name, "patient")
                        userIsComplete = true
                        GlobalObjects.patient = patient
                        GlobalObjects.type = "patient"
                    } else if(doctor.did != "-1") {
                        user = User(doctor.did, doctor.name, "doctor")
                        GlobalObjects.doctor = doctor
                        GlobalObjects.type = "doctor"
                        userIsComplete = true
                    } else {
                        user = User(idToken)
                    }

                    userIsAuthenticated = true
                    appJustLaunched = false
                }

            })
    }

    fun logout() {
        WebAuthProvider
            .logout(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    // For some reason, logout failed.
                    Log.e(TAG, "Error occurred in logout(): $error")
                }

                override fun onSuccess(result: Void?) {
                    // The user successfully logged out.
                    user = User()
                    userIsAuthenticated = false
                }

            })
    }

    fun setContext(activityContext: Context) {
        context = activityContext
        account = Auth0(
            context.getString(R.string.com_auth0_client_id),
            context.getString(R.string.com_auth0_domain)
        )
    }

}