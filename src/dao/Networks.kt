package moe.msm.dao

import moe.msm.model.Network
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
object Networks : LongIdTable() {
    val uuid = uuid("uuid").uniqueIndex()
    val name = varchar("name", 255)
    val machine = reference("machine_id", Machines)
}

class NetworkDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<NetworkDAO>(Networks)

    var uuid by Networks.uuid
    var name by Networks.name
    var machine by MachineDAO referencedOn Networks.machine

    fun toModel() = Network(id.value, uuid, name, machine.toModel())
}