package com.momento.mystoic

import com.momento.mystoic.data.AppContainer
import org.junit.Assert.assertNotNull
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