package moe.msm

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */

class ValidationException(message: String) : RuntimeException(message)
class RequestBodyNotFoundException(message: String = "Request Body is required") : RuntimeException(message)
class ParameterNotFoundException(vararg fields: String) : RuntimeException(fields.joinToString(",") + " is required")
