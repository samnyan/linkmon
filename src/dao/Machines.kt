package moe.msm.dao

import moe.msm.model.Machine
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
object Machines : LongIdTable() {
    val uuid = uuid("uuid").uniqueIndex()
    val name = varchar("name", 255).uniqueIndex()
}

class MachineDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MachineDAO>(Machines)

    var uuid by Machines.uuid
    var name by Machines.name

    fun toModel() = Machine(id.value, uuid, name)
}