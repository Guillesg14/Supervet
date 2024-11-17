package com.supervet.acceptance.helpers

import com.supervet.ktor.configureMigrations
import com.supervet.ktor.configureRouting
import com.supervet.ktor.configureSecurity
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.jdbi.v3.core.Jdbi


fun testApplicationWithDependencies(body: suspend (Jdbi, HttpClient, MapApplicationConfig) -> Unit) {
    testApplication {
        val postgresContainer = PostgresTestContainer()
        val customConfig = MapApplicationConfig(
            "database.host" to postgresContainer.host,
            "database.port" to postgresContainer.port,
            "database.database" to postgresContainer.database,
            "database.user" to postgresContainer.user,
            "database.password" to postgresContainer.password,
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