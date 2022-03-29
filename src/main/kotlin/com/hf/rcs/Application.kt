package com.hf.rcs

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.hf.rcs.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
//        configureSerialization()
//        configureMonitoring()
//        configureSecurity()
    }.start(wait = true)
}
