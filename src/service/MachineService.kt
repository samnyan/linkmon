package moe.msm.service

import moe.msm.dao.MachineDAO
import moe.msm.model.Machine
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
class MachineService {
    fun getAllMachine() =
        transaction {
            MachineDAO.all().map { machineDAO -> machineDAO.toModel() }
        }
}