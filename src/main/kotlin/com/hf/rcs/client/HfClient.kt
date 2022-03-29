package com.hf.rcs.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*

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

    @OptIn(InternalAPI::class)
    suspend fun authorize(): HttpResponse {
        val path = "/login"

        return client.post("$url$path") {
            parameter("country", "US")
            parameter("locale", "en-US")

            body = FormDataContent(Parameters.build {
                append("username", "")
                append("password", "")
            })
        }
    }

    suspend fun search(token: String? = null): HttpResponse {
        val path = "/recipes/recipes/6203c4328c77620a052d7425"
        return client.get("$url$path") {
            parameter("country", "US")
            parameter("locale", "en-US")
            token?.let {
                header("Authorization", "Bearer $token")
            }
        }
    }
}