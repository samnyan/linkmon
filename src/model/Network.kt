package moe.msm.model

import moe.msm.ValidationException
import java.util.*

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
class Network(
    val id: Long?,
    val uuid: UUID?,
    val name: String?,
    val machine: Machine?
) {
    fun validate(): Boolean = if (name != null) true else throw ValidationException("Name is required")
}