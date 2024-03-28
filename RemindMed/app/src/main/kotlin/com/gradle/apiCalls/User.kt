package com.gradle.apiCalls

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.google.gson.JsonParser
import kotlinx.serialization.encodeToString

@OptIn(DelicateCoroutinesApi::class)
class User {

    private val host: String = "https://dev-cog1thu33l6uz3pp.us.auth0.com"
    //TODO: Hide if possible.
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
                            //TODO: Figure out why R.string.com_auth0_api_client_id displays a different value.
                            Json.encodeToString(mapOf(
                                "client_id" to "ZWR8P2MkE3FUeUqYahfIkVzSpyoEPXzE",
                                "client_secret" to "pV_iHLgnnoHEyD0PLoSDn7wlBzWKDevjIpVufXy1eIS_ICUlQ9uVvOWnGoi2m3TK",
                                "audience" to "https://dev-cog1thu33l6uz3pp.us.auth0.com/api/v2/",
                                "grant_type" to "client_credentials"
                            ))
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
                    println("Changing email for user with id: $id")
                    val res = client.patch("$host/api/v2/users/$auth0Id") {
                        headers {
                            append(HttpHeaders.Authorization
                                , "Bearer $apiToken"
                            )
                            append(HttpHeaders.ContentType, ContentType.Application.Json)
                        }
                        setBody(
                            Json.encodeToString(mapOf(
                                "email" to newEmail
                            ))
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
