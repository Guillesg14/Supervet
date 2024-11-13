package com.supervet.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun Application.configureMigrations() {
    val host = environment.config.property("database.host").getString()
    val port = environment.config.property("database.port").getString()
    val database = environment.config.property("database.database").getString()
    val user = environment.config.property("database.user").getString()
    val password = environment.config.property("database.password").getString()

    val hikariConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://${host}:${port}/${database}?user=${user}&password=${password}"
        driverClassName = "org.postgresql.Driver"
    }

    Flyway.configure().loggers("slf4j").dataSource(HikariDataSource(hikariConfig)).load().migrate()
}