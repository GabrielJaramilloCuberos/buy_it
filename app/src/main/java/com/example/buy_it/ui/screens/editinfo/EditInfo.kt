package com.example.buy_it.ui.screens.editinfo

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buy_it.R
import com.example.buy_it.ui.components.FondoBlancoEditInfo
import com.example.buy_it.ui.components.MainButton
import com.example.buy_it.ui.components.PanelGlass
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
        androidx.compose.animation.core.MutableTransitionState(false).apply { targetState = true }
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
                        initialOffsetY = { it / 8 },
                        animationSpec = androidx.compose.animation.core.spring(
                            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                        )
                    )
        ) {
            Box(contentAlignment = Alignment.Center) {
                PanelGlass(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 34.dp, vertical = 36.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    PictureWithCircle(uiState.profileImage)

                    if (uiState.errormsg != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.errormsg!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    PickImage(
                        action = { onImageChange(it) }
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Editar perfil",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Actualiza tu información personal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        FormFieldLabel(text = stringResource(R.string.nombre))
                        TextInput(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = stringResource(R.string.buy_it),
                            item = uiState.name,
                            onItemChange = onNameChange
                        )

                        FormFieldLabel(text = stringResource(R.string.email))
                        TextInput(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = stringResource(R.string.buyit_buyit_com),
                            item = uiState.email,
                            onItemChange = onEmailChange
                        )

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

                    Spacer(modifier = Modifier.height(34.dp))

                    MainButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        text = stringResource(R.string.guardar_cambios),
                        onClick = onSaveChanges
                    )

                    Spacer(modifier = Modifier.height(24.dp))
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
fun PickImage(
    action: (uri:Uri) -> Unit = {}
){

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        Log.d("PickImage", "Callback disparado. Uri: $uri")
        uri?.let {
            Log.d("PickImage", uri.toString())
            action(uri)
        }
    }

    Button(
        onClick = {
            launcher.launch("image/*")
        },
    ) {
        Text(text = "Seleccionar imagen")
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