package com.hf.rcs.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class HfClient {

    val url  = "https://gw-staging.hellofresh.com/api"

    suspend fun search(): String {

        val path = "/recipes/search"
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get("$url$path") {
            parameter("weeks", "2")
            parameter("take", "20")
        }
        return response.toString()
    }

}