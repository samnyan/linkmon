package moe.msm.controller

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.thymeleaf.ThymeleafContent

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
fun Route.webController() {
    route("/web") {
        get("/") {
            call.respond(ThymeleafContent("index", mapOf()))
        }
    }
}