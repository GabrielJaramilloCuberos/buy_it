package com.example.buy_it.ui.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Detail(
    productId: String,
    onBackPressed: () -> Unit,
    onSeeStores: () -> Unit,
    onOpenReviewEditor: (String) -> Unit,
    onEditReview: (String, String) -> Unit,
    onNavigateToProfile: (String) -> Unit,
    detailViewModel: DetailViewModel,
    modifier: Modifier = Modifier,
) {
    val state by detailViewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        detailViewModel.loadProductDetail(productId)
    }

    LaunchedEffect(state.navigateToProfileUserId) {
        state.navigateToProfileUserId?.let { userId ->
            onNavigateToProfile(userId)
            detailViewModel.onNavigationHandled()
        }
    }

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            androidx.compose.material3.CircularProgressIndicator()
        }
        return
    }

    val product = state.product ?: return

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DetailTopBar(
                onBackPressed = onBackPressed
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    ProductHeaderCard(
                        name = product.name,
                        imageRes = product.image,
                        description = product.description,
                        onClickArrow = onSeeStores
                    )
                }

                item {
                    SectionTitle(text = "Opiniones del producto")
                }

                itemsIndexed(state.reviews) { index, review ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(600, delayMillis = 400 + (index * 100))) +
                                slideInVertically(
                                    initialOffsetY = { 50 },
                                    animationSpec = tween(600, delayMillis = 400 + (index * 100))
                                )
                    ) {
                        ReviewMiniCard(
                            info = review,
                            onClick = {
                                if (detailViewModel.isReviewOwner(review.userId)) {
                                    onEditReview(productId, review.id)
                                }
                            },
                            onUserClick = { detailViewModel.onUserClicked(review.userId) },
                            onLikeClick = {
                                detailViewModel.onReviewLikeClicked(review.id)
                            }
                        )
                    }
                }

                item {
                    androidx.compose.foundation.layout.Spacer(
                        modifier = Modifier.padding(bottom = 90.dp)
                    )
                }
            }
        }

        AddReviewButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            onClick = { onOpenReviewEditor(productId) }
        )
    }
}