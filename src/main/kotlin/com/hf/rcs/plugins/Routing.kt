package com.hf.rcs.plugins

import com.hf.rcs.client.HfClient
import com.hf.rcs.data.AuthResponse
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Application.configureRouting() {

    //setup authentication
    install(Authentication) {
        basic("auth-basic") {
            validate { credentials ->
                if (credentials.name == "jetbrains" && credentials.password == "foobar") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    //setup routing and use authentication
    routing {
        authenticate("auth-basic") {
            rcsRouting()
        }
        hfRouting()
    }
}

val hfClient = HfClient()
var token: String? = null

fun Route.hfRouting() {
    get("/recipes") {
        val response: HttpResponse = try {
            hfClient.search(token)
        } catch (exception: ClientRequestException) {
            authorize()
            hfClient.search(token)
        }
        call.respondText { response.bodyAsText() }
    }
}

suspend fun authorize() {
    val authResponse = hfClient.authorize()
    token = if (authResponse.status == HttpStatusCode.OK) {
        val format = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
        val response = format.decodeFromString<AuthResponse>(authResponse.bodyAsText())
        response.accessToken
    } else {
        null
    }
}

