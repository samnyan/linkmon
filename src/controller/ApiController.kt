package moe.msm.controller

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import moe.msm.ParameterNotFoundException
import moe.msm.model.Machine
import moe.msm.model.Network
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
            call.respond(machineService.getAll())
        }

        post("/machine") {
            val m = call.receive<Machine>()
            if (m.validate()) call.respond(machineService.create(m))
        }

        get("/machine/{uuid}") {
            call.respond(machineService.getByUUID(UUID.fromString(call.parameters["uuid"])))
        }

        delete("/machine/{uuid}") {
            machineService.deleteByUUID(UUID.fromString(call.parameters["uuid"]))
            call.respond(HttpStatusCode.NoContent)
        }

        get("/machine/{uuid}/network") {
            call.respond(networkService.getByMachineUUID(UUID.fromString(call.parameters["uuid"])))
        }

        get("/network") {
            call.respond(networkService.getAll())
        }

        post("/machine/{uuid}/network") {
            val n = call.receive<Network>()
            if (n.validate()) call.respond(networkService.create(n, UUID.fromString(call.parameters["uuid"])))
        }

        get("/network/{uuid}") {
            call.respond(networkService.getByUUID(UUID.fromString(call.parameters["uuid"])))
        }

        delete("/network/{uuid}") {
            networkService.deleteByUUID(UUID.fromString(call.parameters["uuid"]))
            call.respond(HttpStatusCode.NoContent)
        }

        get("/network/{uuid}/record") {
            val size: Int = call.parameters["size"]?.toInt() ?: 50
            val offset: Long = call.parameters["offset"]?.toLong() ?: 0
            call.respond(recordService.getByNetworkUUID(UUID.fromString(call.parameters["uuid"]), size, offset))
        }

        get("/submit") {
            val param = call.request.queryParameters
            val networkUUID: String = param["network"] ?: throw ParameterNotFoundException("Network")
            val latency: String? = param["latency"]
            val isUp: String? = param["isUp"]
            call.respond(recordService.saveRecord(networkUUID, latency, isUp))
        }
    }

}