package moe.msm

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import freemarker.cache.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.jackson.*
import io.ktor.features.*
import moe.msm.controller.apiController
import moe.msm.dao.*
import moe.msm.model.Machine
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.reflect.typeOf

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private fun hikari(): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.h2.Driver"
    config.jdbcUrl = "jdbc:h2:mem:test"
    config.maximumPoolSize = 3
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

    // Add some test data
    transaction {
        SchemaUtils.create(Machines)
        SchemaUtils.create(Networks)
        SchemaUtils.create(Records)
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
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {

        apiController()

        get("/") {
            call.respondText("Server Running", contentType = ContentType.Text.Plain)
        }

        get("/html-freemarker") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }
    }
}

data class IndexData(val items: List<Int>)

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
