package moe.msm

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.routing
import moe.msm.controller.apiController
import moe.msm.dao.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// Load database config
private fun hikari(): HikariDataSource {
    val c = ConfigFactory.load().getConfig("ktor.security.dataSource")
    val config = HikariConfig()
    config.driverClassName = c.getString("driverClassName")
    config.jdbcUrl = c.getString("jdbcUrl")
    config.username = c.getString("username")
    config.password = c.getString("password")
    config.maximumPoolSize = c.getInt("maximumPoolSize")
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}

@ExperimentalStdlibApi
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    Database.connect(hikari())

    transaction {
        // Initialize database schema here. Should switch a better database migration tool
        SchemaUtils.createMissingTablesAndColumns(Machines, Networks, Records)
        commit()
        // Add some test data
        if (MachineDAO.count() == 0L) {
            val m = MachineDAO.new {
                name = "Test"
                uuid = UUID.randomUUID()
            }
            val n = NetworkDAO.new {
                machine = m
                name = "Test"
                uuid = UUID.randomUUID()
            }
            RecordDAO.new {
                network = n
                latency = 10
                isUp = true
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JavaTimeModule())
        }
    }

    routing {

        apiController()

        get("/") {
            call.respondRedirect("/web")
        }

        static("/web") {
            resources("web")
            defaultResource("index.html", "web")
        }

    }
}
