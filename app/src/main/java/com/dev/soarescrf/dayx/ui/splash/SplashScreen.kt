package com.dev.soarescrf.dayx.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.dev.soarescrf.dayx.R

/**
 * Tela de splash exibida durante a inicialização do aplicativo.
 *
 * Esta tela apresenta:
 * - Uma animação contínua de gradiente em movimento;
 * - O logotipo centralizado do aplicativo.
 *
 * A animação é criada utilizando um [Animatable], que desloca a posição
 * inicial e final do gradiente, gerando o efeito visual dinâmico.
 */
@Composable
fun SplashScreen() {
    // Controla o deslocamento do gradiente animado
    val gradientShift = remember { Animatable(0f) }

    // Inicia a animação assim que o composable entra em composição
    LaunchedEffect(Unit) {
        gradientShift.animateTo(
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    // Cores utilizadas no gradiente animado
    val colors = listOf(
        Color(0xFF0F0F0F),
        Color(0xFF1C1C1C),
        Color(0xFF2E2E2E),
        Color(0xFF1A1A1A)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = colors,
                    start = Offset(0f, gradientShift.value),
                    end = Offset(gradientShift.value, 0f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = "Logo"
        )
    }
}
