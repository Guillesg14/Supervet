package com.supervet.acceptance.helpers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.testcontainers.containers.PostgreSQLContainer

class PostgresTestContainer {

    val jdbi: Jdbi
    val host: String
    val port: String
    val database: String
    val user: String
    val password: String


    init {
        val container = PostgreSQLContainer("postgres:16-alpine").apply {
            withExposedPorts(5432)
            withReuse(true)
            start()
        }

        host = container.host
        port = container.getMappedPort(5432).toString()
        database = container.databaseName
        user = container.username
        password = container.password

        val connectionString = "jdbc:postgresql://${host}:${port}/${database}?user=${user}&password=${password}"

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = connectionString
            driverClassName = "org.postgresql.Driver"
        }

        jdbi = Jdbi.create(HikariDataSource(hikariConfig))
            .installPlugin(PostgresPlugin())
            .installPlugin(KotlinPlugin(enableCoroutineSupport = true))
    }
}