package com.example.buy_it.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buy_it.ui.theme.Buy_itTheme

@Composable
fun FondoBlanco(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "fondo_blanco")
    
    val moveX by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "moveX"
    )
    val moveY by infiniteTransition.animateFloat(
        initialValue = -40f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "moveY"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 150.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-32).dp + moveX.dp, y = moveY.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .size(270.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 100.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 135.dp - moveX.dp, y = 200.dp + moveY.dp)
                .graphicsLayer(scaleX = 1.1f - scale / 2, scaleY = 1.1f - scale / 2)
                .size(287.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
            colorEnd = MaterialTheme.colorScheme.surface,
            radio = 200.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-16).dp + moveY.dp, y = 450.dp - moveX.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .size(270.dp)
        )
    }
}

@Composable
fun FondoBlancoRegister(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "fondo_register")
    val moveX by infiniteTransition.animateFloat(
        initialValue = -40f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "moveX"
    )
    val moveY by infiniteTransition.animateFloat(
        initialValue = -25f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "moveY"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 120.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-40).dp + moveX.dp, y = (-110).dp - moveY.dp)
                .size(239.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 100.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 105.dp + moveY.dp, y = 20.dp + moveX.dp)
                .size(270.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
            colorEnd = MaterialTheme.colorScheme.surface,
            radio = 140.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp - moveX.dp, y = (-150).dp + moveY.dp)
                .size(200.dp)
        )
    }
}

@Composable
fun FondoBlancoEditInfo(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "fondo_edit")
    val move by infiniteTransition.animateFloat(
        initialValue = -25f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "move"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 100.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier.align(Alignment.Center).offset(x = (-85).dp + move.dp, y = (-50).dp + move.dp).size(239.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 120.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier.align(Alignment.TopEnd).offset(x = 35.dp - move.dp, y = 20.dp + move.dp).size(270.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
            colorEnd = MaterialTheme.colorScheme.surface,
            radio = 140.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f,
            modifier = Modifier.align(Alignment.BottomEnd).offset(x = 40.dp + move.dp, y = (-150).dp - move.dp)
                .size(200.dp)
        )
    }
}

@Composable
fun PanelGlass(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(352.dp)
            .height(800.dp)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(size = 25.dp)
            ),
        contentAlignment = Alignment.TopCenter
    ) {}
}

@Composable
fun ProfileCircles(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "profile_circles")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -25f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "float"
    )
    val rotateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotate"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 120.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.8f,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-40).dp + floatAnim.dp)
                .graphicsLayer(rotationZ = rotateAnim)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
            colorEnd = MaterialTheme.colorScheme.surface,
            radio = 130.dp,
            angulo = -101f,
            inicioGradiente = 0.1f,
            finGradiente = 0.5f,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-60).dp - floatAnim.dp, y = (-120).dp + floatAnim.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
            colorEnd = MaterialTheme.colorScheme.surface,
            radio = 130.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.5f,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 60.dp + floatAnim.dp, y = (-120).dp - floatAnim.dp)
        )
    }
}

@Composable
fun MainBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    val move1 by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "move1"
    )
    
    val move2 by infiniteTransition.animateFloat(
        initialValue = -45f,
        targetValue = 45f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "move2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 85.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.6f,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 50.dp + move1.dp, y = (-40).dp + move2.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.surface,
            colorEnd = MaterialTheme.colorScheme.background,
            radio = 190.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.5f,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-200).dp + move2.dp, x = move1.dp)
        )
        Elipse(
            colorStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            colorEnd = MaterialTheme.colorScheme.surface,
            radio = 125.dp,
            angulo = 90f,
            inicioGradiente = 0.1f,
            finGradiente = 1f,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-140).dp + move1.dp, y = (-110).dp + move2.dp)
        )
    }
}

@Preview
@Composable
fun MainpREVIEW() {
    Buy_itTheme { MainBackground() }
}
