package com.rbrauwers.newsapp.network

import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.SourcesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

internal class NewsHttpClient : NetworkDataSource {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object: Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
        }

        defaultRequest {
            url("https://newsapi.org/v2/")
            headers.appendIfNameAbsent("X-Api-Key", "7b3a48cc5cc24801bdd280dde215dcd0")
        }
    }

    override suspend fun getSources(): SourcesResponse {
        return httpClient.get("top-headlines/sources").body()
    }

    override suspend fun getHeadlines(country: String): HeadlinesResponse {
        return httpClient.get("top-headlines").body()
    }

}