package com.hf.rcs.client

import com.hf.rcs.data.AuthResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class HfClient {


    private val client = HttpClient(CIO) {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials("", "")
                }
            }
        }
    }
    private val url = ""

    var token: String? = null

    @OptIn(InternalAPI::class)
    suspend fun authorize() {
        val path = "/login"

        val response = client.post("$url$path") {
            parameter("country", "US")
            parameter("locale", "en-US")

            body = FormDataContent(Parameters.build {
                append("username", "")
                append("password", "")
            })
        }

        token = if (response.status == HttpStatusCode.OK) {
            val format = Json {
                isLenient = true
                ignoreUnknownKeys = true
            }
            val authResponse = format.decodeFromString<AuthResponse>(response.bodyAsText())
            authResponse.accessToken
        } else {
            null
        }
    }

    suspend fun search(): HttpResponse {

        val path = "/recipes/recipes/6203c4328c77620a052d7425"
        return client.get("$url$path") {
//            parameter("weeks", "2")
//            parameter("take", 20)
            parameter("country", "US")
            parameter("locale", "en-US")
            header("Authorization", "Bearer $token")
        }
    }

}