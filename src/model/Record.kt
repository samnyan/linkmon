package moe.msm.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
class Record(
    val id: Long,
    @JsonIgnore
    val network: Network,
    val latency: Long?,
    val isUp: Boolean?,
    val time: LocalDateTime
)