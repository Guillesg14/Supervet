package com.supervet.acceptance.helpers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.testcontainers.containers.PostgreSQLContainer

class PostgresTestContainer {
    val connectionString: String
    val jdbi: Jdbi

    init {
        val container = PostgreSQLContainer("postgres:16-alpine").apply {
            withExposedPorts(5432)
            withReuse(true)
            start()
        }

        val host = container.host
        val port = container.getMappedPort(5432).toString()
        val database = container.databaseName
        val user = container.username
        val password = container.password

        connectionString = "jdbc:postgresql://${host}:${port}/${database}?user=${user}&password=${password}"

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = connectionString
            driverClassName = "org.postgresql.Driver"
        }

        jdbi = Jdbi.create(HikariDataSource(hikariConfig))
            .installPlugin(PostgresPlugin())
            .installPlugin(KotlinPlugin(enableCoroutineSupport = true))
    }
}