package com.example.buy_it.ui.screens.editinfo

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buy_it.R
import com.example.buy_it.ui.components.FondoBlancoEditInfo
import com.example.buy_it.ui.components.MainButton
import com.example.buy_it.ui.components.PasswordInput
import com.example.buy_it.ui.components.TextInput
import com.example.buy_it.ui.theme.Buy_itTheme

@Composable
fun EditInfo(
    onSaveChanges: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditInfoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    EditInfoContent(
        uiState = uiState,
        onNameChange = { viewModel.onNameChange(it) },
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onToggleMostrarPassword = { viewModel.onToggleMostrarPassword() },
        onImageChange = { viewModel.uploadImageToFirebase(it) },
        onSaveChanges = {
            viewModel.onSaveChanges(
                onSuccess = onSaveChanges
            )
        },
        modifier = modifier
    )
}

@Composable
private fun EditInfoContent(
    uiState: EditInfoState,
    onImageChange: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onToggleMostrarPassword: () -> Unit,
    onSaveChanges: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icono = if (!uiState.mostrarPassword) R.drawable.hide else R.drawable.see

    val visibleState = remember {
        androidx.compose.animation.core.MutableTransitionState(initialState = false).apply { targetState = true }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FondoBlancoEditInfo()

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
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Editar Perfil",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Actualiza tu información personal",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Panel elevado premium
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
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Foto de perfil y botón de cambio
                        Box(contentAlignment = Alignment.BottomCenter) {
                            PictureWithCircle(uiState.profileImage)
                        }

                        PickImage(
                            action = { onImageChange(it) }
                        )

                        if (uiState.errormsg != null) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = uiState.errormsg!!,
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.labelMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Campos del formulario
                        Column {
                            FormFieldLabel(text = stringResource(R.string.nombre))
                            TextInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = stringResource(R.string.buy_it),
                                item = uiState.name,
                                onItemChange = onNameChange
                            )
                        }

                        Column {
                            FormFieldLabel(text = stringResource(R.string.email))
                            TextInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = stringResource(R.string.buyit_buyit_com),
                                item = uiState.email,
                                onItemChange = onEmailChange
                            )
                        }

                        Column {
                            FormFieldLabel(text = stringResource(R.string.contrasenna))
                            PasswordInput(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = stringResource(R.string.contrasenna),
                                item = uiState.password,
                                onItemChange = onPasswordChange,
                                icono = icono,
                                mostrar = uiState.mostrarPassword,
                                onMostrarPassword = onToggleMostrarPassword
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        MainButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp),
                            text = stringResource(R.string.guardar_cambios),
                            onClick = onSaveChanges
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
fun PickImage(
    action: (uri:Uri) -> Unit = {}
){
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { action(it) }
    }

    Button(
        onClick = { launcher.launch("image/*") },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = "Cambiar imagen", fontWeight = FontWeight.SemiBold)
    }
}

@Composable
@Preview(showBackground = true)
fun EditInfoPreview() {
    Buy_itTheme {
        EditInfoContent(
            uiState = EditInfoState(
                name = "Buy It",
                email = "buyit@buyit.com",
                password = "123456",
                mostrarPassword = false
            ),
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onToggleMostrarPassword = {},
            onImageChange = {},
            onSaveChanges = {}
        )
    }
}
