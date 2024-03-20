package com.gradle.models

import android.util.Log
import com.auth0.android.jwt.JWT


data class User(val idToken: String? = null, var type: String = "", var name: String = "") {

    private val TAG = "User"

    var id = ""
    var email = ""

    init {
        if (idToken != null) {
            try {
                // Attempt to decode the ID token.
                val jwt = JWT(idToken ?: "")
                var idString = jwt.subject ?: "1"

                // The ID token is a valid JWT,
                // so extract information about the user from it.
                if(idString.startsWith("auth0|")) {
                    idString = idString.slice(6 until idString.length)
                }
                id = idString
                name = jwt.getClaim("name").asString() ?: ""
                email = jwt.getClaim("email").asString() ?: ""
            } catch (error: com.auth0.android.jwt.DecodeException) {
                // The ID token is NOT a valid JWT, so log the error
                // and leave the user properties as empty strings.
                Log.e(TAG, "Error occurred trying to decode JWT: ${error.toString()} ")
            }
        } else {
            // The User object was instantiated with a null value,
            // which means the user is being logged out.
            // The user properties will be set to empty strings.
            Log.d(TAG, "User is logged out - instantiating empty User object.")
        }
    }

}