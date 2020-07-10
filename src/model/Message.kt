package moe.msm.model

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
data class ErrorMessage(val exception: String, val message: String) {
    constructor(e: Throwable) : this(e.javaClass.simpleName, e.message ?: "")
}