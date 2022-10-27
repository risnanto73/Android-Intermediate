package com.tiorisnanto.storyapp_risnanto73

import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun generateDummyMapsEntity(): List<ListStoriesItem> {
        val storiesList = ArrayList<ListStoriesItem>()
        for (i in 0..10) {
            val news = ListStoriesItem(
                "Photo URL Stories $i",
                "2022-10-27T22:22:22Z",
                "Stories $i ",
                "Stories Description",
                "$i",
                7.3,
                7.3
            )
            storiesList.add(news)
        }
        return storiesList
    }

    fun singleDummy(): ListStoriesItem {
        return ListStoriesItem(
            "Photo URL Stories 1",
            "2022-10-27T22:22:22Z",
            "Stories 1",
            "Stories Description",
            "1",
            7.3,
            7.3
        )
    }

    fun generateDummyStoriesResponse(): AllStoriesResponse {
        return AllStoriesResponse(generateDummyListStories(), false, "Success")
    }

    fun generateDummyListStories(): List<ListStoriesItem> {
        val items: MutableList<ListStoriesItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoriesItem(
                "Photo URL Stories $i",
                "2022-10-27T22:22:22Z",
                "Stories $i ",
                "Stories Description",
                "$i",
                7.3,
                7.3
            )
            items.add(stories)
        }
        return items
    }

    private fun generateDummyLoginResult(): LoginResult {
        return LoginResult(
            "Risnanto73",
            "1",
            "lorem"
        )
    }

    fun generateDummyApiResponseSuccess(): ApiResponse {
        return ApiResponse(
            false,
            "success"
        )
    }

    fun generateDummyLoginResponseSuccess(): LoginResponse {
        return LoginResponse(
            loginResult = generateDummyLoginResult(),
            error = false,
            message = "Success"
        )
    }

    fun generateDummyUserModel(): UserModel {
        return UserModel(
            name = "Risnanto73",
            email = "tiolast63@gmail.com",
            password = "Porenjer71298_",
            userId = "1",
            token = "lorem",
            isLogin = true
        )
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

}