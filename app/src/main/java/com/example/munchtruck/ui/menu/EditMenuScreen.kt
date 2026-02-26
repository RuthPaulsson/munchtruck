package com.example.munchtruck.ui.menu

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.toMessage
import com.example.munchtruck.util.MenuItemValidationError
import com.example.munchtruck.viewmodels.MenuViewModel
import kotlinx.coroutines.launch

@Composable
fun EditMenuScreen(
    navController: NavController,
    viewModel: MenuViewModel,
    itemId: String? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditing = itemId != null
    val errorString = (uiState.error as? MenuItemValidationError)?.toMessage()
        ?: uiState.error?.toString()

    var hasLoadedItem by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf<String?>(null) }

    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    val nameEmptyError = MenuItemValidationError.NameEmpty.toMessage()
    val priceInvalidError = MenuItemValidationError.PriceInvalidFormat.toMessage()



    LaunchedEffect(Unit) {
        viewModel.observeMenu()
    }

    LaunchedEffect(uiState.menuItems, itemId) {
        if (!hasLoadedItem && isEditing && itemId != null) {
            val item = uiState.menuItems.find { it.id == itemId }
            item?.let {
                name = it.name
                price = (it.price / 100.0).toString()
                description = it.description
                hasLoadedItem = true
            }
        }
    }


    val successMessage = stringResource(R.string.menu_item_saved)
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar(successMessage)
            navController.popBackStack()
            viewModel.resetSaveState()
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null && errorString != null) {
            snackbarHostState.showSnackbar(errorString)
            viewModel.clearError()
        }
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    EditMenuContent(
        name = name,
        price = price,
        description = description,
        selectedImageUri = selectedImageUri,
        isEditing = isEditing,
        isLoading = uiState.isLoading,
        priceError = priceError,
        onNameChange = { name = it },
        onPriceChange = {
            val filtered = it.filter { char ->
                char.isDigit() || char == ',' || char == '.'
            }

            val separatorCount =
                filtered.count { c -> c == ',' || c == '.' }

            if (separatorCount <= 1) {
                price = filtered
            }

            priceError = null
        },
        onDescriptionChange = { description = it },
        onBackClick = { navController.popBackStack() },
        onSaveClick = {

            if (name.isBlank()) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(nameEmptyError)
                }
                return@EditMenuContent
            }

            val normalized = price.replace(",", ".")
            val parsed = normalized.toBigDecimalOrNull()

            if (parsed == null || parsed <= java.math.BigDecimal.ZERO) {
                priceError = priceInvalidError
                return@EditMenuContent
            }

            val priceLong = parsed
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .multiply(100.toBigDecimal())
                .toLong()

            if (isEditing && itemId != null) {
                viewModel.updateMenuItem(
                    itemId = itemId,
                    name = name,
                    price = priceLong,
                    description = description,
                    imageUri = selectedImageUri
                )
            } else {
                viewModel.addMenuItem(
                    name = name,
                    price = priceLong,
                    description = description,
                    imageUri = selectedImageUri
                )
            }
        },
        onImageClick = {
            imageLauncher.launch("image/*")
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}