package moe.msm.model

import moe.msm.ValidationException
import java.util.*

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
data class Machine(
    val id: Long?,
    val uuid: UUID?,
    val name: String?
) {
    fun validate(): Boolean = if (name != null) true else throw ValidationException("Name is required")
}
