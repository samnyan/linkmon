package moe.msm.dao

import moe.msm.model.Record
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
object Records: LongIdTable() {
    val network = reference("network_id", Networks)
    val latency = long("latency")
    val isUp = bool("is_up")
}

class RecordDAO(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<RecordDAO>(Records)
    var network by NetworkDAO referencedOn Records.network
    var latency by Records.latency
    var isUp by Records.isUp

    fun toModel() = Record(id.value, network.toModel(), latency, isUp)
}