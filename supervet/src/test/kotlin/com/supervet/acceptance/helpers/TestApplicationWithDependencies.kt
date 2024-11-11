package com.supervet.acceptance.helpers

import com.supervet.plugins.configureMigrations
import com.supervet.plugins.configureRouting
import com.supervet.plugins.configureSecurity
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin

fun testApplicationWithDependencies(body: suspend (Jdbi, HttpClient, MapApplicationConfig) -> Unit) {
    testApplication {
        val postgresContainer = PostgresTestContainer()
        val customConfig = MapApplicationConfig(
            "database.connectionString" to postgresContainer.connectionString
        )

        environment {
            config = customConfig
        }

        application {
            configureMigrations()
            configureSecurity()
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }

        startApplication()

        body(postgresContainer.jdbi, client, customConfig)
    }
}