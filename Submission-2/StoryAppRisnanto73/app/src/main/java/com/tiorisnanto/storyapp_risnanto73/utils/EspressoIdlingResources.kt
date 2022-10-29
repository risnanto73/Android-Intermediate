package com.tiorisnanto.storyapp_risnanto73.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResources = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResources.increment()
    }

    fun decrement() {
        if (!countingIdlingResources.isIdleNow) {
            countingIdlingResources.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    EspressoIdlingResource.increment() // Set apps as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement() // Set apps as idle.
    }
}