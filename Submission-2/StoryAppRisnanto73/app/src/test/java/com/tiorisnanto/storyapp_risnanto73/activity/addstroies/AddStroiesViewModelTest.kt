package com.tiorisnanto.storyapp_risnanto73.activity.addstroies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ApiResponse
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import com.tiorisnanto.storyapp_risnanto73.data.resultresponse.ResultResponse
import com.tiorisnanto.storyapp_risnanto73.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStroiesViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var addStoriesViewModel: AddStroiesViewModel
    private val dummyResponseError = "error"
    private val dummyResponse = ApiResponse(
        false,
        "success"
    )

    @Before
    fun setUp() {
        addStoriesViewModel = AddStroiesViewModel(storiesRepository)
    }

    @Test
    fun `when postStories() is called Should Not Null and Return Success`() {
        val file = Mockito.mock(File::class.java)
        val description = "Description".toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "image", file.name, requestImageFile
        )
        val latitude = "6.1750".toRequestBody("text/plain".toMediaType())
        val longitude = "106.8283".toRequestBody("text/plain".toMediaType())

        val expectedResponse = MutableLiveData<ResultResponse<ApiResponse>>()
        expectedResponse.value = ResultResponse.Success(dummyResponse)
        Mockito.`when`(
            addStoriesViewModel.postStories(
                "Token",
                description,
                imageMultipart,
                latitude,
                longitude
            )
        ).thenReturn(
            expectedResponse
        )

        val actualResponse =
            addStoriesViewModel.postStories("Token", description, imageMultipart, latitude, longitude)
                .getOrAwaitValue()

        Mockito.verify(storiesRepository)
            .postStories("Token", description, imageMultipart, latitude, longitude)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is ResultResponse.Success)
        assertEquals(dummyResponse, (actualResponse as ResultResponse.Success).data)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val file = Mockito.mock(File::class.java)
        val description = "Description".toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "image", file.name, requestImageFile
        )

        val expectedResponse = MutableLiveData<ResultResponse<ApiResponse>>()
        expectedResponse.value = ResultResponse.Error(dummyResponseError)
        Mockito.`when`(addStoriesViewModel.postStories("Token", description, imageMultipart)).thenReturn(
            expectedResponse
        )

        val actualResponse =
            addStoriesViewModel.postStories("Token", description, imageMultipart).getOrAwaitValue()

        Mockito.verify(storiesRepository).postStories("Token", description, imageMultipart)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is ResultResponse.Error)
    }
}