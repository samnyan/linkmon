package moe.msm.dao

import moe.msm.model.Record
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
object Records : LongIdTable() {
    val network = reference("network_id", Networks)
    val latency = long("latency").default(-1)
    val isUp = bool("is_up").default(true)
    val time = datetime("time").default(LocalDateTime.now())
}

class RecordDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RecordDAO>(Records)

    var network by NetworkDAO referencedOn Records.network
    var latency by Records.latency
    var isUp by Records.isUp
    var time by Records.time

    fun toModel() = Record(id.value, network.toModel(), latency, isUp, time)
}