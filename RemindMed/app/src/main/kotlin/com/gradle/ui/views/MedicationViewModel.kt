package com.gradle.ui.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.gradle.apiCalls.Medication as MedicationApi
import com.gradle.models.Medication
import com.gradle.ui.views.ISubscriber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.sql.Time
import java.time.LocalDate
import java.sql.Date

@OptIn(ExperimentalMaterial3Api::class)
class MedicationViewModel(val model: Medication) : ISubscriber {
    var medicationId = mutableStateOf(model.medicationId)
    var name = mutableStateOf(model.name)
    var amount = mutableStateOf(model.amount)
    var startDate = mutableStateOf(model.startDate)
    var endDate = mutableStateOf(model.endDate)
    var notes = mutableStateOf(model.notes)
    var _timeStates = mutableStateListOf<TimePickerState>()
    var timeStates: List<TimePickerState> = _timeStates
    var times = mutableStateOf(model.times)


    var successfulAdd = mutableStateOf(model.successfulAdd)
    var successfulChange = mutableStateOf(model.successfulChange)
    var errorMessage = mutableStateOf(model.errorMessage)
    var isError = mutableStateOf(model.isError)

    init {
        model.subscribe(this)
        _timeStates.add(TimePickerState(0,0,false))
    }

    fun clearAll() {
        medicationId.value = ""
        name.value = ""
        amount.value = ""
        startDate.value = Date(0L)
        endDate.value = Date(0L)
        notes.value = ""
        _timeStates.clear()
        _timeStates.add(TimePickerState(0, 0, false))
        times.value.clear()
        successfulAdd.value = false
        successfulChange.value = false
        errorMessage.value = ""
        isError.value = false
    }




    fun addTimePickerState() {
        _timeStates.add(TimePickerState(0,0,false))
    }

    fun removeTimePickerState(index: Int) {
        if (_timeStates.size > 1) {
            _timeStates.removeAt(index)
        }
    }

    fun getSelectedTimes(): MutableList<Time> {
        val selectedTimes = mutableListOf<Time>()
        for (timeState in _timeStates) {
            selectedTimes.add(Time(timeState.hour, timeState.minute, 0))
        }
        return selectedTimes
    }

    fun getSelectedFormattedTimes(): List<String> {
        return timeStates.map { timeState ->
            val formattedHour = if (timeState.hour == 0 || timeState.hour == 12) "12" else String.format("%02d", timeState.hour % 12)
            val paddedMinute = String.format("%02d", timeState.minute)
            val period = if (timeState.hour < 12) "AM" else "PM"
            "$formattedHour:$paddedMinute $period"
        }
    }
//    private val medicationApi = MedicationApi()
//
//    private val _medicationName = MutableStateFlow("")
//    val medicationName: StateFlow<String> = _medicationName

//    fun getMedicationByName(name: String) {
//        viewModelScope.launch {
//            val result = medicationApi.getMedicationbyName(name)
//            // Process result
//        }
//    }
//
//    // Function to add medication
//    fun addMedication(medication: Medication) {
//        viewModelScope.launch {
//            val success = medicationApi.addMedication(medication)
//            // Handle success or error
//        }
//    }
    override fun update() {
        medicationId.value = model.medicationId
        name.value = model.name
        amount.value = model.amount
        startDate.value = model.startDate
        endDate.value = model.endDate
        notes.value = model.notes
        times.value = model.times
        successfulAdd.value = model.successfulAdd
        successfulChange.value = model.successfulChange
        errorMessage.value = model.errorMessage
        isError.value = model.isError
    }
}
