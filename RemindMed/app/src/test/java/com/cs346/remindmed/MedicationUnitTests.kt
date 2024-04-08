package com.cs346.remindmed

import com.gradle.controller.MedicationController
import com.gradle.models.Medication
import com.gradle.ui.viewModels.MedicationViewModel
import com.gradle.ui.views.shared.MedicationViewEvent
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Test
import java.sql.Date
import java.sql.Time

internal class MedicationUnitTests {
    private val mod: Medication = Medication(
        initialPid = "1",
        initialMedicationId = "100",
        initialAmount = "2 pills",
        initialStartDate = Date.valueOf("2024-04-07"),
        initialEndDate = Date.valueOf("2024-04-14"),
        initialName = "Advil",
        initialNotes = "Take with water",
        initialTimes = mutableListOf(Time.valueOf("08:00:00"), Time.valueOf("20:00:00")),
        initialAccepted = true,
        initialTaken = false
    )

    @ExperimentalSerializationApi
    @Test
    fun viewModelTests() {
        val viewModel = MedicationViewModel(mod)

        assertEquals(mod.pid, viewModel.pid.value)
        assertEquals(mod.medicationId, viewModel.medicationId.value)
        assertEquals(mod.amount, viewModel.amount.value)
        assertEquals(mod.startDate, viewModel.startDate.value)
        assertEquals(mod.endDate, viewModel.endDate.value)
        assertEquals(mod.name, viewModel.name.value)
        assertEquals(mod.notes, viewModel.notes.value)
        assertEquals(mod.times, viewModel.times.value)
        assertEquals(mod.accepted, viewModel.accepted.value)
        assertEquals(mod.taken, viewModel.taken.value)
    }

    @ExperimentalSerializationApi
    @Test
    fun controllerTests() {
        // Separate controller instances for each test
        val controller1 = MedicationController(mod)
        controller1.invoke(MedicationViewEvent.NameEvent, "Updated Medicine Name")
        assertEquals("Updated Medicine Name", mod.name)

        val controller2 = MedicationController(mod)
        controller2.invoke(MedicationViewEvent.AmountEvent, "1 pill")
        assertEquals("1 pill", mod.amount)

        val controller3 = MedicationController(mod)
        val updatedStartDate = Date.valueOf("2024-04-08")
        controller3.invoke(MedicationViewEvent.StartDateEvent, updatedStartDate)
        assertEquals(updatedStartDate, mod.startDate)

        val controller4 = MedicationController(mod)
        val updatedEndDate = Date.valueOf("2024-04-15")
        controller4.invoke(MedicationViewEvent.EndDateEvent, updatedEndDate)
        assertEquals(updatedEndDate, mod.endDate)

        val controller5 = MedicationController(mod)
        controller5.invoke(
            MedicationViewEvent.TimeEvent,
            mutableListOf(Time.valueOf("09:00:00"), Time.valueOf("21:00:00"))
        )
        assertEquals(
            mutableListOf(Time.valueOf("09:00:00"), Time.valueOf("21:00:00")),
            mod.times
        )

        val controller6 = MedicationController(mod)
        controller6.invoke(MedicationViewEvent.AcceptedEvent, false)
        assertEquals(false, mod.accepted)

        val controller7 = MedicationController(mod)
        controller7.invoke(MedicationViewEvent.TakenEvent, true)
        assertEquals(true, mod.taken)
    }
}
