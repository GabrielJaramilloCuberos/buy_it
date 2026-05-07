package com.example.buy_it.ui.screens.createreview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.buy_it.ui.components.TextInput
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.example.buy_it.ui.components.MainButton


@Composable
fun Createreview(
    onReviewSubmitted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var uiState by remember { mutableStateOf(CreateReviewState()) }

    CreateReviewContent(
        uiState = uiState,
        onProductNameChange = { uiState = uiState.copy(productName = it) },
        onCommentChange = { uiState = uiState.copy(comment = it) },
        onSubmit = {
            onReviewSubmitted()
        },
        modifier = modifier
    )
}

@Composable
private fun CreateReviewContent(
    uiState: CreateReviewState,
    onProductNameChange: (String) -> Unit,
    onCommentChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleState = remember {
        androidx.compose.animation.core.MutableTransitionState(false).apply { targetState = true }
    }

    Box(modifier = modifier.fillMaxSize()) {
        com.example.buy_it.ui.components.MainBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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
                androidx.compose.material3.Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Nueva Reseña",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Column(modifier = Modifier.fillMaxWidth()) {
                            FormFieldLabel(text = "Producto")
                            TextInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "Nombre del producto a reseñar",
                                item = uiState.productName,
                                onItemChange = onProductNameChange
                            )
                        }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            FormFieldLabel(text = "Tu opinión")
                            TextInput(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                placeholder = "Escribe aquí lo que piensas del producto...",
                                item = uiState.comment,
                                onItemChange = onCommentChange
                            )
                        }

                        if (uiState.errorMessage != null) {
                            Text(
                                text = uiState.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        MainButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            text = "Publicar Reseña",
                            onClick = onSubmit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
private fun FormFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
    )
}
