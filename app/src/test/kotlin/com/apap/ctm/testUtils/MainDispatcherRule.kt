package com.apap.ctm.testUtils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.ExternalResource

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
) : ExternalResource() {

    override fun before()  {
        Dispatchers.setMain(dispatcher)
    }

    override fun after() {
        Dispatchers.resetMain()
    }
}