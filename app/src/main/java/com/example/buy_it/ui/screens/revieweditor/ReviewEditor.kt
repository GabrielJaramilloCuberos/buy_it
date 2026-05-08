package com.example.buy_it.ui.screens.revieweditor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buy_it.ui.components.LoadingAnimation
import com.example.buy_it.ui.components.MainBackground

@Composable
fun ReviewEditor(
    productId: String = "",
    reviewId: String? = null,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewEditorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId, reviewId) {
        viewModel.loadEditor(productId = productId, reviewId = reviewId)
    }

    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            onBackPressed()
            viewModel.onNavigatedBack()
        }
    }

    val visibleState = remember {
        androidx.compose.animation.core.MutableTransitionState(false).apply { targetState = true }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        MainBackground()

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(1000)) +
                    slideInVertically(
                        initialOffsetY = { it / 8 },
                        animationSpec = androidx.compose.animation.core.spring(
                            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                        )
                    )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ReviewEditorTopBar(
                    title = if (uiState.isEditMode) "Editar reseña" else "Escribir reseña",
                    onBackPressed = onBackPressed
                )
                
                when {
                    uiState.isLoading && uiState.productName.isBlank() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingAnimation(size = 120)
                        }
                    }

                    uiState.errorMessage != null && uiState.productName.isBlank() -> {
                        Text(
                            text = uiState.errorMessage ?: "Error al cargar",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    else -> {
                        ReviewEditorCard(
                            productName = uiState.productName,
                            productImage = uiState.productImage,
                            opinion = uiState.opinion,
                            onOpinionChange = { viewModel.onOpinionChange(it) },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 10.dp)
                        )

                        if (uiState.isEditMode && !uiState.canEditOrDelete) {
                            Text(
                                text = "Solo puedes editar o eliminar tus propias reseñas.",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        PublishReviewButton(
                            enabled = uiState.canPublish && !uiState.isLoading,
                            text = if (uiState.isEditMode) "Guardar cambios" else "Publicar reseña",
                            onClick = { viewModel.submitReview(productId) },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 18.dp)
                        )

                        uiState.errorMessage?.let { message ->
                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        if (uiState.isEditMode && uiState.canEditOrDelete) {
                            Button(
                                onClick = { viewModel.deleteReview() },
                                enabled = !uiState.isLoading,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp, bottom = 90.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Eliminar reseña")
                            }
                        } else {
                            Spacer(modifier = Modifier.height(90.dp))
                        }

                        if (uiState.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingAnimation(size = 80)
                            }
                        }
                    }
                }
            }
        }
    }
}