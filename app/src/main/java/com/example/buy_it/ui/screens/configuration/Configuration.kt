package com.example.buy_it.ui.screens.configuration

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buy_it.R
import androidx.compose.foundation.background

@Composable
fun Configuration(
    onBackPressed: () -> Unit,
    onLogout: () -> Unit,
    configurationViewModel: ConfigurationViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by configurationViewModel.uiState.collectAsState()

    if(state.navigateToLogin){
        onLogout()
        configurationViewModel.onLogoutHandled()
    }

    ConfigurationScreen(
        state = state,
        onBackPressed = onBackPressed,
        onSearchQueryChanged = { configurationViewModel.onSearchQueryChanged(it) },
        onToggleAccountPrivacy = { configurationViewModel.toggleAccountPrivacy() },
        onLogout = { configurationViewModel.logout() },
        modifier = modifier
    )
}

@Composable
fun ConfigurationScreen(
    state: ConfigurationState,
    onBackPressed: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onToggleAccountPrivacy: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleState = remember {
        androidx.compose.animation.core.MutableTransitionState(false).apply { targetState = true }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(800)) +
                    slideInVertically(initialOffsetY = { 40 })
        ) {
            Image(
                painter = painterResource(R.drawable.arrowleft),
                contentDescription = stringResource(R.string.volver),
                modifier = Modifier
                    .padding(bottom = 18.dp)
                    .height(28.dp)
                    .clickable(onClick = onBackPressed)
            )
        }

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(800, delayMillis = 100)) +
                    slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(800, delayMillis = 100))
        ) {
            Text(
                text = stringResource(R.string.configuraciones),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) +
                    slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(800, delayMillis = 200))
        ) {
            SearchBar(
                value = state.searchQuery,
                onValueChange = onSearchQueryChanged
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(800, delayMillis = 300)) +
                    slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(800, delayMillis = 300))
        ) {
            Column {
                SettingsSectionTitle(
                    text = stringResource(R.string.quien_pude_ver_tus_rese_as)
                )

                Spacer(modifier = Modifier.height(10.dp))

                SubsectionItem(
                    icon = Icons.Default.Lock,
                    text = stringResource(R.string.privacidad_de_la_cuenta),
                    secondText = if (state.isAccountPrivate) stringResource(R.string.privada) else "Pública",
                    onClick = onToggleAccountPrivacy
                )

                SubsectionItem(
                    icon = Icons.Default.NoAccounts,
                    text = stringResource(R.string.bloqueados),
                    secondText = state.blockedCount.toString()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(800, delayMillis = 400)) +
                    slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(800, delayMillis = 400))
        ) {
            Column {
                SettingsSectionTitle(
                    text = stringResource(R.string.general)
                )

                Spacer(modifier = Modifier.height(10.dp))

                SubsectionItem(
                    icon = Icons.Default.Language,
                    text = stringResource(R.string.idioma),
                    secondText = state.currentLanguage
                )

                SubsectionItem(
                    icon = Icons.Default.NotificationsActive,
                    text = stringResource(R.string.notificaciones)
                )

                SubsectionItem(
                    icon = Icons.Default.DarkMode,
                    text = stringResource(R.string.temas),
                    secondText = state.currentTheme
                )

                SubsectionItem(
                    icon = Icons.Default.DataThresholding,
                    text = stringResource(R.string.tiempo_en_pantalla)
                )

                SubsectionItem(
                    icon = Icons.Default.Cloud,
                    text = stringResource(R.string.uso_de_datos)
                )

                SubsectionItem(
                    icon = Icons.Default.Storage,
                    text = stringResource(R.string.almacenamiento)
                )

                SubsectionItem(
                    icon = Icons.Default.ExitToApp,
                    text = stringResource(R.string.cerrar_sesi_n),
                    onClick = onLogout
                )
            }
        }

        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
private fun SettingsSectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
@Preview(showBackground = true)
fun ConfigurationPreview() {
    ConfigurationScreen(
        state = ConfigurationState(),
        onBackPressed = {},
        onSearchQueryChanged = {},
        onToggleAccountPrivacy = {},
        onLogout = {}
    )
}