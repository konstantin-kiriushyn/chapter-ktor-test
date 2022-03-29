package com.hf.rcs.plugins

import com.hf.rcs.data.SingleFeature
import com.hf.rcs.data.simpleFeatures
import com.hf.rcs.data.togglesJson
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.locations.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.request.*

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
    }
}

fun Route.rcsRouting() {
    route("/rcs") {

        //get all features
        get {
            simpleFeatures.sortBy { it.id }
            call.respond(simpleFeatures)
        }

        //add new feature
        post {
            try {
                val newFeature = call.receive<SingleFeature>()
                simpleFeatures.add(newFeature)
                call.respondText("New Feature Added $simpleFeatures", status = HttpStatusCode.Created)
            } catch (exception: ContentTransformationException) {
                call.respondText("Error adding feature ${exception.message}", status = HttpStatusCode.BadRequest)
            }
        }

        //delete a feature
        delete("{id}") {
            val featureToDelete = call.parameters["id"] ?: return@delete call.respondText(
                "Parameter is missing",
                status = HttpStatusCode.BadRequest
            )
            val feature = simpleFeatures.find { it.id == featureToDelete }
            if (feature != null) {
                simpleFeatures.remove(feature)
                call.respondText("Feature was deleted $simpleFeatures", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Feature by Id not found $simpleFeatures", status = HttpStatusCode.NotFound)
            }
        }

        //change value of a feature
        put {
            try {
                val updateFeature = call.receive<SingleFeature>()
                val foundFeature = simpleFeatures.find { it.id == updateFeature.id }
                if (foundFeature != null) {
                    simpleFeatures.remove(foundFeature)
                    simpleFeatures.add(updateFeature)
                    call.respondText("Feature Updated $simpleFeatures", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Feature by Id not found $simpleFeatures", status = HttpStatusCode.NotFound)
                }
            } catch (exception: ContentTransformationException) {
                call.respondText("Bad Request ${exception.message}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}
