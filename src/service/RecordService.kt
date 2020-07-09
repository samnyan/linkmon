package moe.msm.service

import moe.msm.dao.NetworkDAO
import moe.msm.dao.Networks
import moe.msm.dao.RecordDAO
import moe.msm.dao.Records
import moe.msm.model.Record
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

/**
 * @author sam_nya (privateamusement@protonmail.com)
 */
class RecordService {
    fun getByUUID(uuid: UUID): List<Record> {
        val query = Records.innerJoin(Networks).select { Networks.uuid eq uuid }.withDistinct()
        return transaction {
            RecordDAO.wrapRows(query).map { r -> r.toModel() }
        }
    }

    fun saveRecord(networkUUID: String, latency: String?, up: String?): Record {
        val n = transaction {
            NetworkDAO.find { Networks.uuid eq UUID.fromString(networkUUID) }.firstOrNull()
        }
        assert(n != null) { "Network not found" }
        return transaction {
            RecordDAO.new {
                network = n!!
                if (latency != null) this.latency = latency.toLong()
                if (up != null) this.isUp = up.toBoolean()
                time = LocalDateTime.now()
            }.toModel()
        }
    }
}