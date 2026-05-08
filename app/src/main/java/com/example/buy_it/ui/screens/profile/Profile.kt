package com.example.buy_it.ui.screens.profile

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buy_it.R
import com.example.buy_it.data.ProductInfo
import com.example.buy_it.ui.components.ProfileCircles
import com.example.buy_it.ui.screens.editinfo.PictureWithCircle
import com.example.buy_it.ui.screens.home.ProductCard
import com.example.buy_it.ui.theme.Buy_itTheme

@Composable
fun Profile(
    userId: String,
    onProfileEdit: () -> Unit,
    onConfigurationEdit: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTrendsClick: () -> Unit,
    onOpenDetail: (String) -> Unit,
    onFollowersClick: (String) -> Unit = {},
    onFollowingClick: (String) -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by profileViewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        profileViewModel.getUserProfile(userId)
        Log.d("Profile", "UserIdCargado: $userId")
        profileViewModel.getUserReviews(userId)
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        ProfileCircles()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item {
                if (state.isCurrentUser) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 26.dp, end = 4.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    Log.d("Profile", "Icono configuración presionado")
                                    onConfigurationEdit()
                                }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.settings),
                                contentDescription = stringResource(R.string.ajustes),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(30.dp))
                }

                Box(contentAlignment = Alignment.BottomEnd) {
                    PictureWithCircle(profileLink = state.profileImage)

                    if (state.isCurrentUser) {
                        Card(
                            modifier = Modifier
                                .offset(x = 10.dp, y = (-4).dp)
                                .clickable(onClick = onProfileEdit),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.editar_perfil),
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Editar",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                state.user?.let { user ->
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "@${user.username}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileStatItem(
                        value = state.memberSince,
                        label = "Miembro"
                    )

                    ProfileStatItem(
                        value = state.seguidoresCount,
                        label = stringResource(R.string.seguidores),
                        modifier = Modifier.clickable {
                            state.user?.id?.let { onFollowersClick(it) }
                        }
                    )

                    ProfileStatItem(
                        value = state.siguiendoCount,
                        label = "Siguiendo",
                        modifier = Modifier.clickable {
                            state.user?.id?.let { onFollowingClick(it) }
                        }
                    )
                }

                if (!state.isCurrentUser) {
                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { profileViewModel.followOrUnfollowUser() }
                    ) {
                        Text(
                            text = if (state.isFollowing) {
                                "Dejar de seguir"
                            } else {
                                "Seguir"
                            }
                        )
                    }
                }

                if (state.isCurrentUser) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tus reseñas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reseñas de ${state.user?.name ?: ""}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(state.reviews) { index, review ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(600, delayMillis = 400 + index * 100)) +
                            slideInVertically(
                                initialOffsetY = { 50 },
                                animationSpec = tween(600, delayMillis = 400 + index * 100)
                            )
                ) {
                    ProductCard(
                        productInfo = ProductInfo(
                            id = review.productId,
                            name = review.product,
                            image = review.imgProd,
                            description = review.review,
                            likePercent = review.percentageLikes,
                            range = review.range,
                            ratingsCount = review.likesCount
                        ),
                        onClick = {
                            if (profileViewModel.isReviewOwner(review.userId)) {
                                onOpenDetail(review.productId) // O onEditReview si existiera
                            } else {
                                onOpenDetail(review.productId)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProfilePreview() {
    Buy_itTheme {
        Profile(
            onProfileEdit = {},
            onConfigurationEdit = {},
            onHomeClick = {},
            onProfileClick = {},
            onTrendsClick = {},
            onOpenDetail = {},
            userId = "1",
        )
    }
}
