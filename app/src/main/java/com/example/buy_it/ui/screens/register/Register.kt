package com.example.buy_it.ui.screens.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buy_it.R
import com.example.buy_it.ui.components.CheckAndText
import com.example.buy_it.ui.components.FondoBlancoRegister
import com.example.buy_it.ui.components.MainButton
import com.example.buy_it.ui.components.PasswordInput
import com.example.buy_it.ui.components.TextInput
import com.example.buy_it.ui.theme.Buy_itTheme

@Composable
fun Register(
    registerButtonPressed: () -> Unit,
    onBackScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RegisterContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onUsernameChange = viewModel::onUsernameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onAcceptedTermsChange = { viewModel.onAcceptedTermsChange() },
        onToggleMostrarPassword = viewModel::onToggleMostrarPassword,
        onToggleMostrarConfirmPassword = viewModel::onToggleMostrarConfirmPassword,
        onRegister = viewModel::onRegister,
        onBackClicked = viewModel::onBackClicked,
        onNavigationHandled = viewModel::onNavigationHandled,
        registerButtonPressed = registerButtonPressed,
        onBackScreen = onBackScreen,
        modifier = modifier
    )
}

@Composable
fun RegisterContent(
    uiState: RegisterState,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAcceptedTermsChange: (Boolean) -> Unit,
    onToggleMostrarPassword: () -> Unit,
    onToggleMostrarConfirmPassword: () -> Unit,
    onRegister: () -> Unit,
    onBackClicked: () -> Unit,
    onNavigationHandled: () -> Unit,
    registerButtonPressed: () -> Unit,
    onBackScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            registerButtonPressed()
            onNavigationHandled()
        }
    }

    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            onBackScreen()
            onNavigationHandled()
        }
    }

    val iconoPassword = if (!uiState.mostrarPassword) R.drawable.hide else R.drawable.see
    val iconoConfirmPassword = if (!uiState.mostrarConfirmPassword) R.drawable.hide else R.drawable.see

    val visibleState = remember {
        androidx.compose.animation.core.MutableTransitionState(initialState = false).apply { targetState = true }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FondoBlancoRegister()

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(1000)) +
                    slideInVertically(
                        initialOffsetY = { 100 },
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
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header con botón de volver flotante
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onBackScreen,
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(44.dp),
                        shadowElevation = 6.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(R.drawable.arrowleft),
                                contentDescription = stringResource(R.string.volver),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Únete a nuestra comunidad hoy",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Formulario en una sola columna con diseño con profundidad
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    shadowElevation = 12.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.03f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column {
                            FormFieldLabel(text = "Nombre completo")
                            TextInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "Sebastian Angarita",
                                item = uiState.name,
                                onItemChange = onNameChange
                            )
                        }

                        Column {
                            FormFieldLabel(text = "Nombre de usuario")
                            TextInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "micho_dev",
                                item = uiState.username,
                                onItemChange = onUsernameChange
                            )
                        }

                        Column {
                            FormFieldLabel(text = "Correo electrónico")
                            TextInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "ejemplo@correo.com",
                                item = uiState.email,
                                onItemChange = onEmailChange
                            )
                        }

                        Column {
                            FormFieldLabel(text = "Contraseña")
                            PasswordInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "Mínimo 6 caracteres",
                                item = uiState.password,
                                onItemChange = onPasswordChange,
                                icono = iconoPassword,
                                mostrar = uiState.mostrarPassword,
                                onMostrarPassword = onToggleMostrarPassword
                            )
                        }

                        Column {
                            FormFieldLabel(text = "Confirmar contraseña")
                            PasswordInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "Repite tu contraseña",
                                item = uiState.confirmPassword,
                                onItemChange = onConfirmPasswordChange,
                                icono = iconoConfirmPassword,
                                mostrar = uiState.mostrarConfirmPassword,
                                onMostrarPassword = onToggleMostrarConfirmPassword
                            )
                        }

                        CheckAndText(
                            estado = uiState.acceptedTerms,
                            onEstadoChange = onAcceptedTermsChange,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        if (uiState.mostrarMensaje && uiState.errorMessage.isNotEmpty()) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = uiState.errorMessage,
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.labelMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        MainButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp),
                            text = "Empezar ahora",
                            onClick = onRegister
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
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
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
@Preview(showBackground = false)
fun RegisterPreview() {
    Buy_itTheme {
        RegisterContent(
            uiState = RegisterState(),
            onNameChange = {},
            onUsernameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onAcceptedTermsChange = {},
            onToggleMostrarPassword = {},
            onToggleMostrarConfirmPassword = {},
            onRegister = {},
            onBackClicked = {},
            onNavigationHandled = {},
            registerButtonPressed = {},
            onBackScreen = {}
        )
    }
}
