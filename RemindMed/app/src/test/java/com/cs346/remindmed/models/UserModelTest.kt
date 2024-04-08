package com.cs346.remindmed.models

import com.gradle.models.User
import org.junit.Assert.assertEquals
import org.junit.Test

internal class UserModelTest {
    @Test
    fun initializeTest() {
        val user = User("1a", "patient")
        assertEquals(user.type, "patient")
    }

    @Test
    fun initializeNullTest() {
        val user = User(null, "patient", "test user")
        assertEquals(user.id, "")
    }
}