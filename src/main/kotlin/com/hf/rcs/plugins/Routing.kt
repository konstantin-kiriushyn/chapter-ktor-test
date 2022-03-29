package com.hf.rcs.plugins

import com.hf.rcs.data.SingleFeature
import com.hf.rcs.data.simpleFeatures
import com.hf.rcs.data.togglesJson
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.locations.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    routing {
        rcsRouting()
    }
}

fun Route.rcsRouting() {
    route("/rcs") {

        get {
            simpleFeatures.sortBy { it.id }
            call.respond(simpleFeatures)
        }

        post {
            try {
                val newFeature = call.receive<SingleFeature>()
                simpleFeatures.add(newFeature)
                call.respondText("New Feature Added $simpleFeatures", status = HttpStatusCode.Created)
            } catch (exception: ContentTransformationException) {
                call.respondText("Error adding feature ${exception.message}", status = HttpStatusCode.BadRequest)
            }
        }

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
