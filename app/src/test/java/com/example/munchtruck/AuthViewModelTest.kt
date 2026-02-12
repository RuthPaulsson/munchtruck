package com.example.munchtruck


import com.example.munchtruck.data.repository.AuthRepository
import com.example.munchtruck.viewmodels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.*
import java.lang.reflect.Field
import java.util.regex.Pattern

import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {


    private lateinit var mockRepo: AuthRepository
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)


        try {
            val field: Field = android.util.Patterns::class.java.getDeclaredField("EMAIL_ADDRESS")
            field.isAccessible = true
            field.set(null, Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"))
        } catch (e: Exception) {
        }


        mockRepo = mock(AuthRepository::class.java)
        viewModel = AuthViewModel(mockRepo)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `login with valid credentials updates state to logged in`() = runTest {
        val email = "user@test.com"
        val password = "Password123!"

        whenever(mockRepo.isUserLoggedIn()).thenReturn(true)

        viewModel.login(email, password)

        verify(mockRepo).login(email, password)
        assertTrue("Inloggningen borde ha lyckats", viewModel.isLoggedIn.value)
        assertEquals("", viewModel.error.value)
    }


    @Test
    fun `login with wrong credentials shows error message`() = runTest {
        val email = "user@test.com"
        val password = "Password123!"
        val errorMessage = "Login failed"

        whenever(mockRepo.login(any(), any())).thenAnswer {
            throw RuntimeException(errorMessage)
        }

        viewModel.login(email, password)

        assertFalse(viewModel.isLoggedIn.value)
        assertEquals(errorMessage, viewModel.error.value)
    }


    @Test
    fun `logout clears user session and state`() = runTest {
        viewModel.login("user@test.com", "Password123!")


        viewModel.logout()


        assertFalse(viewModel.isLoggedIn.value)
        verify(mockRepo).logout()
    }
    @Test
    fun `register_with_valid_data_calls_repository_and_updates_isLoggedIn`() = runTest {
        val email = "newowner@munchtruck.com"
        val password = "StrongPassword123!"

        whenever(mockRepo.isUserLoggedIn()).thenReturn(true)

        viewModel.register(email, password, password)

        verify(mockRepo).register(email, password)
        assertTrue("Registreringen borde ha lyckats", viewModel.isLoggedIn.value)
        assertEquals("", viewModel.error.value)
    }


    @Test
    fun `register_with_mismatching_passwords_sets_error_and_does_not_call_repository`() = runTest {
        viewModel.register("test@test.com", "Pass123!", "DifferentPass")


        assertTrue(viewModel.error.value.isNotEmpty())


        verify(mockRepo, never()).register(any(), any())
        assertFalse(viewModel.isLoggedIn.value)
    }


    @Test
    fun `register_fails_in_repository_sets_error_message`() = runTest {
        val errorMsg = "The email address is already in use by another account."
        whenever(mockRepo.register(any(), any())).thenThrow(RuntimeException(errorMsg))

        viewModel.register("existing@test.com", "Password123!", "Password123!")

        assertEquals(errorMsg, viewModel.error.value)
        assertFalse(viewModel.isLoggedIn.value)
    }
}
