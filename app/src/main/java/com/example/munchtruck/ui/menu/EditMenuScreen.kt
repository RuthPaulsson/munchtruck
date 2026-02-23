package com.example.munchtruck.ui.menu

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    var hasLoadedItem by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val message = stringResource(R.string.menu_item_saved)
    val invalidNameMessage = stringResource(R.string.invalid_dish_name)
    val invalidPriceMessage = stringResource(R.string.invalid_price)





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


    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar(message)
            navController.popBackStack()
            viewModel.resetSaveState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
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
                    snackbarHostState.showSnackbar(invalidNameMessage)
                }
                return@EditMenuContent
            }

            val normalized = price.replace(",", ".")
            val parsed = normalized.toBigDecimalOrNull()

            if (parsed == null || parsed <= java.math.BigDecimal.ZERO) {
                priceError = invalidPriceMessage
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
        onDeleteClick = {
            showDeleteDialog = true

        },
        onImageClick = {
            imageLauncher.launch("image/*")
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_title)) },
            text = { Text(stringResource(R.string.delete_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false

                        if (itemId != null) {
                            viewModel.deleteMenuItem(itemId)
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }

}