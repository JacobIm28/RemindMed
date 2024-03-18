package com.backend.remindmedapi.models

import kotlinx.serialization.json.JsonObject
import org.postgresql.util.PGobject
import java.sql.Time
import java.sql.Date

data class Medication (
        val pid: Int,
        val medicationId: String,
        val amount: String,
        val startDate: Date,
        val endDate: Date,
        val name: String,
        val notes: String,
        val times: Array<Time>
) {
    override fun toString(): String {
        return "Medication(pid=$pid, medication_id=$medicationId, amount='$amount', start_date=$startDate, end_date=$endDate, times=$times, name=$name, notes=$notes)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Medication

        if (pid != other.pid) return false
        if (medicationId != other.medicationId) return false
        if (amount != other.amount) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (!times.contentEquals(other.times)) return false
        if (name != other.name) return false
        return notes == other.notes
    }

    override fun hashCode(): Int {
        var result = pid
        result = 31 * result + medicationId.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + times.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + notes.hashCode()
        return result
    }
}
