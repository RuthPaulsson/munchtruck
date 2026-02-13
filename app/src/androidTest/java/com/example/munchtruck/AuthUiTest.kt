package com.example.munchtruck

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.munchtruck.ui.navigation.NavGraph
import org.junit.Rule
import org.junit.Test

class AuthUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_access_restriction_after_logout() {
        composeTestRule.setContent {
            NavGraph()
        }

        composeTestRule.onNodeWithText("Owner", substring = true, ignoreCase = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Log out", substring = true, ignoreCase = true)
            .assertDoesNotExist()

        composeTestRule.onNodeWithText("Are you sure you want to log out?").assertDoesNotExist()
    }
}