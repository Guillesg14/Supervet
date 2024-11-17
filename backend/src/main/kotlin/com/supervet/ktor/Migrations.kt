package com.supervet.ktor

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import javax.sql.DataSource

fun Application.configureMigrations() {
    val dataSource by closestDI().instance<DataSource>()
    Flyway.configure().loggers("slf4j").dataSource(dataSource).load().migrate()
}