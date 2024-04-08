package com.cs346.remindmed

import com.gradle.controller.PeopleListController
import com.gradle.models.PeopleList
import com.gradle.ui.viewModels.PeopleListViewModel
import com.gradle.ui.views.shared.PeopleListEvent
import org.junit.Assert.assertEquals
import org.junit.Test

class PeopleListUnitTests {
    @Test
    fun viewModelTest() {
        val model : PeopleList = PeopleList()
        val viewModel : PeopleListViewModel = PeopleListViewModel(model)
        assertEquals(model.doctorList, viewModel.doctorList.value)
        assertEquals(model.patientList, viewModel.patientList.value)
    }

    @Test
    fun controllerTest() {
        val model : PeopleList = PeopleList()
        val viewModel : PeopleListViewModel = PeopleListViewModel(model)
        val controller : PeopleListController = PeopleListController(model)
        controller.invoke(PeopleListEvent.DeleteEvent, "dummy")
        assertEquals(true, viewModel.showDialog.value)
        assertEquals(true, model.showDialog)
    }
}