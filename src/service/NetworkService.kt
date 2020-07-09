package moe.msm.service

import moe.msm.dao.NetworkDAO
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
class NetworkService {
    fun getAllNetwork() =
        transaction {
            NetworkDAO.all().with(NetworkDAO::machine).map { networkDAO -> networkDAO.toModel() }
        }
}