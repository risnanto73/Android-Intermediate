package com.tiorisnanto.storyapp_risnanto73.activity.main

import com.tiorisnanto.storyapp_risnanto73.DataDummy
import com.tiorisnanto.storyapp_risnanto73.MainCoroutineRules
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    var mainCoroutineRule = MainCoroutineRules()

    @Mock
    private lateinit var userPreference: UserPreferences
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(userPreference)
    }

    @Test
    fun `when getUser() is called should no null and Return Success`() = runTest {
        val expectedResponse = flowOf(DataDummy.generateDummyUserModel())
        Mockito.`when`(mainViewModel.getUser()).thenReturn(expectedResponse)

        mainViewModel.getUser().collect {
            assertNotNull(it.token)
            assertEquals(DataDummy.generateDummyUserModel().token, it.token)
        }

        Mockito.verify(userPreference).getUser()
    }

    @Test
    fun `when logOut() is called should Success`() = runTest {
        mainViewModel.logout()
        Mockito.verify(userPreference).logout()
    }

}