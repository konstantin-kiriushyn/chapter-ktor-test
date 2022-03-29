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
    }
}
