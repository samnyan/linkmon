package moe.msm.service

import moe.msm.dao.MachineDAO
import moe.msm.dao.Machines
import moe.msm.model.Machine
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
class MachineService {
    fun getAll() =
        transaction {
            MachineDAO.all().map { machineDAO -> machineDAO.toModel() }
        }

    fun getByUUID(uuid: UUID) =
        transaction {
            MachineDAO.find { Machines.uuid eq uuid }.first().toModel()
        }

    fun create(machine: Machine): Machine =
        transaction {
            MachineDAO.new {
                name = machine.name!!
                uuid = UUID.randomUUID()
            }.toModel()
        }

    fun deleteByUUID(uuid: UUID) {
        transaction {
            Machines.deleteWhere { Machines.uuid eq uuid }
        }
    }
}