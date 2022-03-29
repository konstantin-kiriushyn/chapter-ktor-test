package com.hf.rcs.plugins

import com.hf.rcs.client.HfClient
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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

fun Route.hfRouting() {
    get("/recipes") {
        if (hfClient.token == null) {
            hfClient.authorize()
        }
        val result = hfClient.search()
        call.respondText { result.bodyAsText() }
    }
}

