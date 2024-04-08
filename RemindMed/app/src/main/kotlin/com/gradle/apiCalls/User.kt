package com.gradle.apiCalls

import com.google.gson.JsonParser
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class User {

    private val host: String = "https://dev-cog1thu33l6uz3pp.us.auth0.com"
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            ContentType.Application.Json
        }
    }

    private fun getAccessToken(): String {
        return try {
            var accessToken = ""
            runBlocking {
                launch {
                    println("Getting access token")
                    val res: String = client.post("$host/oauth/token") {
                        headers {
                            append(HttpHeaders.ContentType, ContentType.Application.Json)
                        }
                        setBody(
                            Json.encodeToString(
                                mapOf(
                                    "client_id" to "ZWR8P2MkE3FUeUqYahfIkVzSpyoEPXzE",
                                    "client_secret" to "pV_iHLgnnoHEyD0PLoSDn7wlBzWKDevjIpVufXy1eIS_ICUlQ9uVvOWnGoi2m3TK",
                                    "audience" to "https://dev-cog1thu33l6uz3pp.us.auth0.com/api/v2/",
                                    "grant_type" to "client_credentials"
                                )
                            )
                        )
                    }.bodyAsText()
                    accessToken = JsonParser.parseString(res).asJsonObject["access_token"].asString
                }
            }
            accessToken
        } catch (e: Exception) {
            throw e
        }
    }
    fun changeEmail(id: String, newEmail: String): Boolean {
        val apiToken = getAccessToken()
        return try {
            var success = false
            val auth0Id = "auth0|$id"
            runBlocking {
                launch {
                    val res = client.patch("$host/api/v2/users/$auth0Id") {
                        headers {
                            append(
                                HttpHeaders.Authorization, "Bearer $apiToken"
                            )
                            append(HttpHeaders.ContentType, ContentType.Application.Json)
                        }
                        setBody(
                            Json.encodeToString(
                                mapOf(
                                    "email" to newEmail
                                )
                            )
                        )
                    }
                    success = res.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }
}
