package com.example.buy_it.ui.screens.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        androidx.compose.animation.core.MutableTransitionState(false).apply { targetState = true }
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
                        initialOffsetY = { it / 8 },
                        animationSpec = androidx.compose.animation.core.spring(
                            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                        )
                    )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .fillMaxHeight(0.92f)
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f))
                )

                Image(
                    painter = painterResource(R.drawable.arrowleft),
                    contentDescription = stringResource(R.string.volver),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 24.dp, top = 24.dp)
                        .size(32.dp)
                        .clickable { onBackScreen() }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(72.dp))

                    Text(
                        text = stringResource(R.string.crear_cuenta),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Únete y empieza a compartir opiniones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        FormFieldLabel(text = "Nombre completo")
                        TextInput(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = "Sebastian Angarita",
                            item = uiState.name,
                            onItemChange = { onNameChange(it) }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                FormFieldLabel(text = "Usuario")
                                TextInput(
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = "micho_dev",
                                    item = uiState.username,
                                    onItemChange = { onUsernameChange(it) }
                                )
                            }
                            Column(modifier = Modifier.weight(1.2f)) {
                                FormFieldLabel(text = stringResource(R.string.email))
                                TextInput(
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = "ejemplo@correo.com",
                                    item = uiState.email,
                                    onItemChange = { onEmailChange(it) }
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                FormFieldLabel(text = stringResource(R.string.contrasenna))
                                PasswordInput(
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = "******",
                                    item = uiState.password,
                                    onItemChange = { onPasswordChange(it) },
                                    icono = iconoPassword,
                                    mostrar = uiState.mostrarPassword,
                                    onMostrarPassword = { onToggleMostrarPassword() }
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                FormFieldLabel(text = "Confirmar")
                                PasswordInput(
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = "******",
                                    item = uiState.confirmPassword,
                                    onItemChange = { onConfirmPasswordChange(it) },
                                    icono = iconoConfirmPassword,
                                    mostrar = uiState.mostrarConfirmPassword,
                                    onMostrarPassword = { onToggleMostrarConfirmPassword() }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    CheckAndText(
                        estado = uiState.acceptedTerms,
                        onEstadoChange = onAcceptedTermsChange,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    if (uiState.mostrarMensaje && uiState.errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = uiState.errorMessage,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    MainButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        text = stringResource(R.string.crear_cuenta),
                        onClick = onRegister
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
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
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
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
