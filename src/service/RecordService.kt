package moe.msm.service

import moe.msm.dao.NetworkDAO
import moe.msm.dao.Networks
import moe.msm.dao.RecordDAO
import moe.msm.dao.Records
import moe.msm.model.Record
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
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
}