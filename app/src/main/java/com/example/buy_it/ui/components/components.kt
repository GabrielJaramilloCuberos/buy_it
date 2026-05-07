package com.example.buy_it.ui.components

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buy_it.R
import com.example.buy_it.ui.theme.Buy_itTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Elipse(
    colorStart: Color = MaterialTheme.colorScheme.primary,
    colorEnd: Color = MaterialTheme.colorScheme.onPrimary,
    radio: Dp = 50.dp,
    angulo: Float = 45f,
    inicioGradiente: Float = 0.5f,
    finGradiente: Float = 0.0f,
    modifier: Modifier = Modifier
){
    Canvas(modifier = modifier.size(radio * 2)){
        val radioPX = radio.toPx()
        val anguloRad = Math.toRadians(angulo.toDouble()).toFloat()

        val startX = center.x - cos(anguloRad) * radioPX
        val startY = center.y - sin(anguloRad) * radioPX
        val endX = center.x + cos(anguloRad) * radioPX
        val endY = center.y + sin(anguloRad) * radioPX

        drawCircle(
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    inicioGradiente to colorStart,
                    finGradiente to colorEnd
                ),
                start = Offset(startX, startY),
                end = Offset(endX, endY)
            ),
            radius = radioPX,
            center = center
        )
    }
}

@Composable
@Preview()
fun ElipsePreview(){
    Buy_itTheme {
        Elipse(
            radio = 100.dp,
            angulo = -71f,
            inicioGradiente = 0.1f,
            finGradiente = 0.7f
        )
    }
}

@Composable
fun CompleteELipse(
    @ColorRes colorStart: Int = R.color.graybluebuyit,
    modifier: Modifier = Modifier,
    sizeDraw: Dp = 10.dp,
){
    val color1 = colorResource(id = colorStart)
    Canvas(
        modifier = modifier.size(sizeDraw)
    ) {
        drawCircle(
            color = color1,
            radius = size.minDimension/2,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CompleteElipsePreview(){
    CompleteELipse(sizeDraw = 100.dp)
}


@Composable
fun Circle(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(R.drawable.elipse1),
) {
    Image(
        painter = painter,
        contentDescription = stringResource(R.string.imagen_de_elipse),
        modifier = modifier,
    )
}

@Composable
fun GradientMessage(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 96.sp,
    fontWeight: FontWeight = FontWeight(510),
){
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0.17f to colorResource(R.color.graybluebuyit),
                    1f to colorResource(R.color.navybluebuyit)
                ),
                start = Offset(0f, 0f),
                end = Offset(0.5299f*1000f, -0.848f*100f)
            ),
            textAlign = TextAlign.Center
        ), modifier = modifier
    )
}

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    placeholder: String,
    item: String,
    onItemChange: (String) -> Unit,
) {
    TextField(
        placeholder = {
            Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier,
        value = item,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface
        ),
        onValueChange = onItemChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.outline,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
    )
}

@Composable
fun TextInputRounded(
    modifier: Modifier = Modifier,
    placeholder: String,
    item: String,
    onItemChange: (String) -> Unit,
) {
    TextField(
        placeholder = {
            Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier.clip(RoundedCornerShape(14.dp)),
        value = item,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface
        ),
        onValueChange = onItemChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.outline,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    placeholder: String,
    item: String,
    onItemChange: (String) -> Unit,
    mostrar: Boolean,
    onMostrarPassword: () -> Unit,
    icono: Int,
) {
    TextField(
        value = item,
        onValueChange = onItemChange,
        placeholder = {
            Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        visualTransformation = if (mostrar) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.outline,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        trailingIcon = {
            IconButton(onClick = onMostrarPassword) {
                Icon(
                    painter = painterResource(icono),
                    contentDescription = "Show/Hide Password",
                    modifier = Modifier.size(25.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
fun CheckAndText(
    estado: Boolean = false,
    onEstadoChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp)
    ) {
        Checkbox(
            checked = estado,
            onCheckedChange = onEstadoChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                uncheckedColor = MaterialTheme.colorScheme.outline,
            )
        )
        Text(
            modifier = Modifier.padding(9.dp),
            text = "Recordarme",
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun MainButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100), label = ""
    )

    Button(
        onClick = onClick,
        modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100), label = ""
    )

    Button(
        onClick = onClick,
        modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProfileText(
    modifier: Modifier= Modifier,
    text: String = "",
){
    Text(
        text=text,
        fontSize = 20.sp,
        fontWeight = FontWeight(500)
    )
}

@Composable
fun ProfilePost(
    modifier: Modifier= Modifier,
    img: Int = R.drawable.cafe,
    descripcion: String,
){
    Image(
        painter = painterResource(img),
        contentDescription = descripcion,
        modifier = modifier.size(170.dp)
    )
}

@Composable
fun HomeIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(30.dp)
    ) {
        Icon(
            imageVector = Icons.Default.People,
            contentDescription = "Seguidos",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun AddIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(30.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.agregar),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun BuscarIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(30.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Descubrimientos",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(30.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Perfil",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape),
        )
    }
}

@Composable
fun BarNav(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onBuscarClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    currentRoute: String? = null
) {
    Row(
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(36.dp),
                clip = false
            )
            .clip(RoundedCornerShape(36.dp))
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(
            icon = Icons.Default.Home,
            label = "Inicio",
            isSelected = currentRoute == "home",
            onClick = onHomeClick
        )
        NavItem(
            icon = Icons.Default.Search,
            label = "Buscar",
            isSelected = currentRoute == "trends",
            onClick = onBuscarClick
        )
        NavItem(
            icon = Icons.Default.Person,
            label = "Perfil",
            isSelected = currentRoute == "profile",
            onClick = onProfileClick
        )
    }
}

@Composable
fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        animationSpec = tween(300), label = ""
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(300), label = ""
    )
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = tween(300), label = ""
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(scaleX = iconScale, scaleY = iconScale)
            )
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(start = 8.dp),
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfileAsyncImage(
    profileLink: String?,
    size: Int,
    modifier: Modifier = Modifier
){
    AsyncImage(
        contentDescription = "User image",
        model = ImageRequest.Builder(LocalContext.current)
            .data(profileLink)
            .crossfade(true)
            .build(),
        error = painterResource(id = R.drawable.user_image_icon),
        placeholder = painterResource(id = R.drawable.loading_img),
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(size.dp).clip(CircleShape)
    )
}
