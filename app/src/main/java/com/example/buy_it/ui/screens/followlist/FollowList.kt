package com.example.buy_it.ui.screens.followlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buy_it.ui.components.ProfileAsyncImage

@Composable
fun FollowList(
    userId: String,
    type: String,
    onBackPressed: () -> Unit,
    onUserClick: (String) -> Unit,
    viewModel: FollowListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(userId, type) {
        viewModel.loadUsers(userId, type)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier.clickable { onBackPressed() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (type == "followers") "Seguidores" else "Siguiendo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.padding(top = 18.dp)
        ) {
            itemsIndexed(state.users) { index, user ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(600, delayMillis = index * 100)) +
                            slideInVertically(
                                initialOffsetY = { 50 },
                                animationSpec = tween(600, delayMillis = index * 100)
                            )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clickable { onUserClick(user.id) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileAsyncImage(
                            profileLink = user.pfpURL,
                            size = 45
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = user.name.ifBlank { "Usuario" },
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}