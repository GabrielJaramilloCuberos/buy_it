package com.example.buy_it.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FondoBlanco()

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(1000)) +
                    slideInVertically(
                        initialOffsetY = { 100 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo con mayor impacto
                GradientMessage(
                    text = "buy it.",
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Descubre, compara y comparte opiniones",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(56.dp))

                // Panel elevado estilo premium (igual que registro)
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
                            FormFieldLabel(text = "Correo electrónico")
                            TextInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "ejemplo@correo.com",
                                item = state.email,
                                onItemChange = onEmailChanged
                            )
                        }

                        Column {
                            FormFieldLabel(text = "Contraseña")
                            PasswordInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "Tu contraseña",
                                item = state.password,
                                onItemChange = onPasswordChanged,
                                mostrar = state.isPasswordVisible,
                                onMostrarPassword = togglePasswordVisibility,
                                icono = icono
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CheckAndText(
                                estado = state.isRememberMeChecked,
                                onEstadoChange = { onRememberMeChanged() }
                            )
                        }

                        if (state.mostrarMensaje && state.errorMessage.isNotEmpty()) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = state.errorMessage,
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.labelMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            MainButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(58.dp),
                                text = stringResource(R.string.iniciar_sesion),
                                onClick = onLoginButtonPressed
                            )

                            SecondaryButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(58.dp),
                                text = stringResource(R.string.crear_cuenta),
                                onClick = onRegisterButtonPressed
                            )
                        }
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
