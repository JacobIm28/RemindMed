package com.gradle.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonElement
import com.gradle.utilities.toFormattedDateString
import com.gradle.utilities.toFormattedTimeString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import java.sql.Time
import java.sql.Date

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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(encoder: Encoder, value: Time) {
        encoder.encodeString(value.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): Time {
        return Time.valueOf(decoder.decodeString())
    }
}
//@Serializable
//data class Medication(
//    @SerialName("pid") val pid: String,  @SerialName("amount") val amount: String,
//    @SerialName("startDate") @Serializable(with = DateSerializer::class) val startDate: Date,
//    @SerialName("endDate") @Serializable(with = DateSerializer::class) val endDate: Date,
//    @SerialName("name") val name: String,
//    @SerialName("notes") val notes: String,
//    @SerialName("times") @Serializable(with = TimeSerializer::class) val timesJson: String,
//    @SerialName("medicationId") val medicationId: String,
//
//) {
//    val times: MutableList<Time>
//        get() {
//            val times = mutableListOf<Time>()
//            val json = Json.parseToJsonElement(timesJson).jsonArray
//            for (element in json) {
//                times.add(Time.valueOf(element.jsonPrimitive.content))
//            }
//            return times
//        }
@Serializable
class Medication(
    var initialPid: String,
    var initialMedicationId: String,
    var initialAmount: String,
    @Serializable(with = DateSerializer::class) var initialStartDate: Date,
    @Serializable(with = DateSerializer::class) var initialEndDate: Date,
    var initialName: String,
    var initialNotes: String,
    var initialTimes: MutableList<@Serializable(with = TimeSerializer::class) Time>
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

    var name: String = initialName
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

//    override fun notifySubscribers() {
//        // Notification logic here
//    }
    override fun toString(): String {
        return "Medication(pid=$pid, medicationId=$medicationId, amount='$amount', startDate=${startDate}, endDate=${endDate}, name=$name, notes=$notes) times=${times}"
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
//        if (!times.contentEquals(other.times)) return false
        if (name != other.name) return false
        return notes == other.notes
    }

    override fun hashCode(): Int {
        var result = pid.hashCode()
        result = 31 * result + medicationId.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
//        result = 31 * result + times.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + notes.hashCode()
        return result
    }
}
