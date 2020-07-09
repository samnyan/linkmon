package moe.msm.controller

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import moe.msm.service.MachineService
import moe.msm.service.NetworkService
import moe.msm.service.RecordService
import java.util.*

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
fun Route.apiController() {
    val machineService = MachineService()
    val networkService = NetworkService()
    val recordService = RecordService()

    route("/api") {
        get {
            call.respondText { "API OK" }
        }

        get("/machine") {
            call.respond(machineService.getAllMachine())
        }

        get("/network") {
            call.respond(networkService.getAllNetwork())
        }

        get("/network/{uuid}") {
            call.respond(recordService.getByUUID(UUID.fromString(call.parameters["uuid"])))
        }
    }

}