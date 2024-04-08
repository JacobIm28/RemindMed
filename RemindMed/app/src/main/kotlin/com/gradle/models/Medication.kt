package com.gradle.models

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Date
import java.sql.Time

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Date::class)
object DateSerializer {
    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date.valueOf(decoder.decodeString())
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Time::class)
object TimeSerializer : KSerializer<Time> {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun serialize(encoder: Encoder, value: Time) {
        encoder.encodeString(value.toString())
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun deserialize(decoder: Decoder): Time {
        return Time.valueOf(decoder.decodeString())
    }
}

@Serializable
class Medication(
    var initialPid: String,
    var initialMedicationId: String,
    var initialAmount: String,
    @Serializable(with = DateSerializer::class) var initialStartDate: Date,
    @Serializable(with = DateSerializer::class) var initialEndDate: Date,
    var initialName: String,
    var initialNotes: String,
    var initialTimes: MutableList<@Serializable(with = TimeSerializer::class) Time>,
    var initialAccepted: Boolean,
    var initialTaken: Boolean
) : IPresenter() {

    var pid: String = initialPid
        set(value) {
            field = value
            notifySubscribers()
        }

    var medicationId: String = initialMedicationId
        set(value) {
            field = value
            notifySubscribers()
        }
    var name: String = initialName
        set(value) {
            field = value
            notifySubscribers()
        }

    var amount: String = initialAmount
        set(value) {
            field = value
            notifySubscribers()
        }

    var startDate: @Serializable(with = DateSerializer::class) Date = initialStartDate
        set(value) {
            field = value
            notifySubscribers()
        }

    var endDate: @Serializable(with = DateSerializer::class) Date = initialEndDate
        set(value) {
            field = value
            notifySubscribers()
        }

    var notes: String = initialNotes
        set(value) {
            field = value
            notifySubscribers()
        }

    var times: MutableList<@Serializable(with = TimeSerializer::class) Time> = initialTimes
        set(value) {
            field = value
            notifySubscribers()
        }

    var accepted = initialAccepted
        set(value) {
            field = value
            notifySubscribers()
        }

    var taken = initialTaken
        set(value) {
            field = value
            notifySubscribers()
        }

    var successfulAdd = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var successfulChange = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var successfulRemove = false
        set(value) {
            field = value
            notifySubscribers()
        }

    var errorMessage = ""
        set(value) {
            field = value
            notifySubscribers()
        }

    var isError = false
        set(value) {
            field = value
            notifySubscribers()
        }

    override fun toString(): String {
        return "Medication(pid=$pid, medicationId=$medicationId, amount='$amount', startDate=${startDate}, endDate=${endDate}, name=$name, notes=$notes, times=${times}, accepted=$accepted, taken=$taken)"
    }

    fun getFormattedTimes(): List<String> {
        return times.map { time ->
            val formattedHour = if (time.hours == 0 || time.hours == 12) "12" else String.format(
                "%02d",
                time.hours % 12
            )
            val paddedMinute = String.format("%02d", time.minutes)
            val period = if (time.hours < 12) "AM" else "PM"
            "$formattedHour:$paddedMinute $period"
        }
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
        if (name != other.name) return false
        if (accepted != other.accepted) return false
        if (taken != other.taken) return false
        return notes == other.notes
    }

    override fun hashCode(): Int {
        var result = pid.hashCode()
        result = 31 * result + medicationId.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + accepted.hashCode()
        result = 31 * result + taken.hashCode()
        return result
    }
}
