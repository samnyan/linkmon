package moe.msm.service

import moe.msm.dao.MachineDAO
import moe.msm.dao.Machines
import moe.msm.dao.NetworkDAO
import moe.msm.dao.Networks
import moe.msm.model.Network
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
class NetworkService {
    fun getAll() =
        transaction {
            NetworkDAO.all().with(NetworkDAO::machine).map { networkDAO -> networkDAO.toModel() }
        }

    fun getByUUID(uuid: UUID) =
        transaction {
            NetworkDAO.find { Networks.uuid eq uuid }.first().toModel()
        }

    fun getByMachineUUID(uuid: UUID): List<Network> {
        val query = Networks.innerJoin(Machines).select { Machines.uuid eq uuid }.withDistinct()
        return transaction {
            NetworkDAO.wrapRows(query).map { n -> n.toModel() }
        }
    }

    fun create(n: Network, machineUUID: UUID): Network =
        transaction {
            val m = MachineDAO.find { Machines.uuid eq machineUUID }.first()
            NetworkDAO.new {
                name = n.name!!
                uuid = UUID.randomUUID()
                machine = m
            }.toModel()
        }

    fun deleteByUUID(uuid: UUID) {
        transaction {
            Networks.deleteWhere { Networks.uuid eq uuid }
        }
    }
}