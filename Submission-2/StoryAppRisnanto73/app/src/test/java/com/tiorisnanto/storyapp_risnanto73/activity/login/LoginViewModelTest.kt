package com.tiorisnanto.storyapp_risnanto73.activity.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tiorisnanto.storyapp_risnanto73.DataDummy
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.LoginResult
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import com.tiorisnanto.storyapp_risnanto73.data.resultresponse.ResultResponse
import com.tiorisnanto.storyapp_risnanto73.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyResult = DataDummy.generateDummyLoginResponseSuccess().loginResult

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storiesRepository)
    }

    @Test
    fun `when login() is Called Should Return Success and Data`() {
        val expectedResponse = MutableLiveData<ResultResponse<LoginResult>>()
        expectedResponse.value = ResultResponse.Success(dummyResult)
        Mockito.`when`(loginViewModel.login("Risnanto73", "Porenjer71298_")).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login("Risnanto73", "Porenjer71298_").getOrAwaitValue()

        Mockito.verify(storiesRepository).login("Risnanto73", "Porenjer71298_")
        assertNotNull(actualResponse)
        assertTrue(actualResponse is ResultResponse.Success)
        assertEquals(dummyResult, (actualResponse as ResultResponse.Success).data)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResponse = MutableLiveData<ResultResponse<LoginResult>>()
        expectedResponse.value = ResultResponse.Error("Error")
        Mockito.`when`(loginViewModel.login("Risnanto73", "Porenjer71298_")).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login("Risnanto73", "Porenjer71298_").getOrAwaitValue()

        Mockito.verify(storiesRepository).login("Risnanto73", "Porenjer71298_")
        assertNotNull(actualResponse)
        assertTrue(actualResponse is ResultResponse.Error)
    }
}