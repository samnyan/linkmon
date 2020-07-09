package moe.msm.model

import moe.msm.dao.Networks
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
class Network(
    val id: Long,
    val uuid: UUID,
    val name: String,
    val machine: Machine
)