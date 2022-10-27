package com.tiorisnanto.storyapp_risnanto73.activity.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tiorisnanto.storyapp_risnanto73.DataDummy
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ApiResponse
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import com.tiorisnanto.storyapp_risnanto73.data.resultresponse.ResultResponse
import com.tiorisnanto.storyapp_risnanto73.getOrAwaitValue
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyResponse = DataDummy.generateDummyApiResponseSuccess()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storiesRepository)
    }

    @Test
    fun `when register() is Called Should Not Null and Return Success`() {
        val expectedResponse = MutableLiveData<ResultResponse<ApiResponse>>()
        expectedResponse.value = ResultResponse.Success(dummyResponse)
        Mockito.`when`(registerViewModel.register("Risnanto", "Risnanto73", "PasswordUser")).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.register("Risnanto", "Risnanto73", "PasswordUser").getOrAwaitValue()

        Mockito.verify(storiesRepository).register("Risnanto", "Risnanto73", "PasswordUser")
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ResultResponse.Success)
        Assert.assertEquals(dummyResponse, (actualResponse as ResultResponse.Success).data)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResponse = MutableLiveData<ResultResponse<ApiResponse>>()
        expectedResponse.value = ResultResponse.Error("Error")
        Mockito.`when`(registerViewModel.register("Risnanto", "Risnanto73", "PasswordUser")).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.register("Risnanto", "Risnanto73", "PasswordUser").getOrAwaitValue()

        Mockito.verify(storiesRepository).register("Risnanto", "Risnanto73", "PasswordUser")
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ResultResponse.Error)
    }
}