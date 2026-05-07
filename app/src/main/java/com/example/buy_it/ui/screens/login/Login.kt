package com.example.buy_it.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.buy_it.R
import com.example.buy_it.ui.components.CheckAndText
import com.example.buy_it.ui.components.FondoBlanco
import com.example.buy_it.ui.components.GradientMessage
import com.example.buy_it.ui.components.MainButton
import com.example.buy_it.ui.components.PasswordInput
import com.example.buy_it.ui.components.SecondaryButton
import com.example.buy_it.ui.components.TextInput

@Composable
fun Login(
    loginViewModel: LoginViewModel,
    onRegisterButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by loginViewModel.uiState.collectAsState()
    LoginContent(
        state = state,
        onEmailChanged = { loginViewModel.onEmailChanged(it) },
        onPasswordChanged = { loginViewModel.onPasswordChanged(it) },
        onRememberMeChanged = { loginViewModel.onRememberMeChanged() },
        togglePasswordVisibility = { loginViewModel.togglePasswordVisibility() },
        onLoginButtonPressed = { loginViewModel.loginButtonPressed() },
        onRegisterButtonPressed = onRegisterButtonPressed,
        modifier = modifier
    )
}

@Composable
fun LoginContent(
    state: LoginState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRememberMeChanged: () -> Unit,
    togglePasswordVisibility: () -> Unit,
    onLoginButtonPressed: () -> Unit,
    onRegisterButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icono = if (!state.isPasswordVisible) R.drawable.hide else R.drawable.see

    // Estado para disparar las animaciones
    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FondoBlanco()

        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título con animación
                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = fadeIn(animationSpec = tween(800)) + slideInVertically(initialOffsetY = { 40 })
                ) {
                    GradientMessage(
                        text = "buy it.",
                        fontSize = 56.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción con ligero retraso
                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) +
                            slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(800, delayMillis = 200))
                ) {
                    Text(
                        text = "Descubre, compara y comparte opiniones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Inputs con retraso
                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 400)) +
                            slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(800, delayMillis = 400))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextInput(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = "Email",
                            item = state.email,
                            onItemChange = onEmailChanged
                        )

                        PasswordInput(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = "Contraseña",
                            item = state.password,
                            onItemChange = onPasswordChanged,
                            mostrar = state.isPasswordVisible,
                            onMostrarPassword = togglePasswordVisibility,
                            icono = icono
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Checkbox
                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 500))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CheckAndText(
                            estado = state.isRememberMeChecked,
                            onEstadoChange = { onRememberMeChanged() }
                        )
                    }
                }

                if (state.mostrarMensaje && state.errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botones con retraso final
                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 600)) +
                            slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(800, delayMillis = 600))
                ) {
                    Column {
                        MainButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            text = stringResource(R.string.iniciar_sesion),
                            onClick = onLoginButtonPressed
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SecondaryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            text = stringResource(R.string.crear_cuenta),
                            onClick = onRegisterButtonPressed
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginPreview() {
    LoginContent(
        state = LoginState(),
        onEmailChanged = {},
        onPasswordChanged = {},
        onRememberMeChanged = {},
        togglePasswordVisibility = {},
        onLoginButtonPressed = {},
        onRegisterButtonPressed = {}
    )
}
