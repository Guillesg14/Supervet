package com.supervet.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun Application.configureMigrations() {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = environment.config.property("database.connectionString").getString()
        driverClassName = "org.postgresql.Driver"
    }

    Flyway.configure().loggers("slf4j").dataSource(HikariDataSource(hikariConfig)).load().migrate()
}