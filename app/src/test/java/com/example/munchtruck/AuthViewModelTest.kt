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

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var mockRepo: AuthRepository
    private lateinit var viewModel: AuthViewModel
    // UnconfinedTestDispatcher gör att coroutinen körs direkt utan fördröjning
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // --- AKUTFIX FÖR PATTERNS ---
        // Detta tvingar in ett värde i Androids systemkod så att det inte är null
        try {
            val field: Field = android.util.Patterns::class.java.getDeclaredField("EMAIL_ADDRESS")
            field.isAccessible = true
            field.set(null, Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"))
        } catch (e: Exception) {
            // Ignorera om det redan är satt
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
        val password = "Password123!" // Viktigt: Måste passera valideringen!

        // Act
        viewModel.login(email, password)

        // Assert
        assertTrue("Inloggningen borde ha lyckats", viewModel.isLoggedIn.value)
        assertEquals("", viewModel.error.value)
    }

    @Test
    fun `login with wrong credentials shows error message`() = runTest {
        val email = "user@test.com"
        val password = "Password123!"
        val errorMessage = "Login failed"

        // Simulera att repository kastar ett fel
        whenever(mockRepo.login(any(), any())).thenAnswer {
            throw RuntimeException(errorMessage)
        }

        // Act
        viewModel.login(email, password)

        // Assert
        assertFalse(viewModel.isLoggedIn.value)
        assertEquals(errorMessage, viewModel.error.value)
    }

    @Test
    fun `logout clears user session and state`() = runTest {
        // Vi simulerar först en inloggning genom att anropa login direkt
        viewModel.login("user@test.com", "Password123!")

        // Act
        viewModel.logout()

        // Assert
        assertFalse(viewModel.isLoggedIn.value)
        verify(mockRepo).logout()
    }
}