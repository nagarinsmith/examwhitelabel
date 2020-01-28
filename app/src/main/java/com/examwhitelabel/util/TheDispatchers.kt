package com.examwhitelabel.util

import kotlinx.coroutines.CoroutineDispatcher

interface TheDispatchers {

    val Main: CoroutineDispatcher
    val IO: CoroutineDispatcher
}
