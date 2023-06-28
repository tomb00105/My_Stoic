package com.example.mystoic

import com.example.mystoic.data.AppContainer
import com.example.mystoic.data.AppDataContainer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MyStoicApplicationTest {
    lateinit var myStoicApplication: MyStoicApplication

    @Before
    fun setup() {
        myStoicApplication = MyStoicApplication()
    }
    @Test
    fun initializeContainer_returnsContainer() {
        val container : AppContainer = myStoicApplication.initializeContainer()
        assertNotNull(container)
    }
}