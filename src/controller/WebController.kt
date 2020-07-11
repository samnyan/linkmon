package moe.msm.controller

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.thymeleaf.ThymeleafContent
import moe.msm.ParameterNotFoundException
import moe.msm.service.MachineService
import moe.msm.service.NetworkService
import moe.msm.service.RecordService
import java.util.*

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
fun Route.webController() {
    val machineService = MachineService()
    val networkService = NetworkService()
    val recordService = RecordService()

    route("/web") {
        get("/") {
            call.respondRedirect("/web/machine")
        }

        get("/machine") {
            call.respond(ThymeleafContent("machine/index", mapOf("machines" to machineService.getAll())))
        }

        get("/machine/{uuid}") {
            val uuid: UUID = UUID.fromString(call.parameters["uuid"] ?: throw ParameterNotFoundException("uuid"))
            call.respond(
                ThymeleafContent(
                    "machine/detail",
                    mapOf(
                        "machine" to machineService.getByUUID(uuid),
                        "networks" to networkService.getByMachineUUID(uuid)
                    )
                )
            )
        }

        get("/network") {
            call.respond(ThymeleafContent("network/index", mapOf("networks" to networkService.getAll())))
        }

        get("/network/{uuid}") {
            val uuid: UUID = UUID.fromString(call.parameters["uuid"] ?: throw ParameterNotFoundException("uuid"))
            call.respond(
                ThymeleafContent(
                    "network/detail",
                    mapOf(
                            "network" to networkService.getByUUID(uuid),
                            "records" to recordService.getByNetworkUUID(uuid, 50, 0, true)
                    )
                )
            )
        }
    }
}