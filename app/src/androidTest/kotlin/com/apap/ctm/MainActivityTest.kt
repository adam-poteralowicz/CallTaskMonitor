package com.apap.ctm

import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import com.apap.ctm.presentation.view.MainActivity
import com.apap.ctm.presentation.view.MainScreen
import com.apap.ctm.presentation.viewmodel.MainViewModel
import com.apap.ctm.testUtils.FakeAppDatabase
import com.apap.ctm.testUtils.FakeLogRepository
import com.apap.ctm.ui.theme.CallTaskMonitorTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: MainViewModel
    private lateinit var logRepository: FakeLogRepository
    private lateinit var appDatabase: FakeAppDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            CallTaskMonitorTheme {
                MainScreen("127.0.0.1")
            }
        }
        logRepository = FakeLogRepository()
        appDatabase = FakeAppDatabase()
        initViewModel()
    }

    @Test
    fun verify_turn_server_on_button_visible() = with(composeTestRule) {
        waitUntil {
            onAllNodesWithText("Turn server ON").fetchSemanticsNodes().size == 1
        }
    }

    private fun initViewModel() {
        viewModel = MainViewModel(logRepository)
    }
}