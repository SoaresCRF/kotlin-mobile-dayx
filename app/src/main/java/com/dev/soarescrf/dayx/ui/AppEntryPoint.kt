package com.dev.soarescrf.dayx.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dev.soarescrf.dayx.ui.main.DayXApp
import com.dev.soarescrf.dayx.ui.splash.SplashScreen
import kotlinx.coroutines.delay

/**
 * Ponto de entrada da interface do aplicativo.
 *
 * Este composable controla a exibição inicial da tela de splash, fazendo a transição
 * automática para o conteúdo principal após um breve intervalo.
 *
 * Funcionamento:
 * - Exibe [SplashScreen] por aproximadamente 2.6 segundos.
 * - Ao término do delay, ativa a navegação principal através de [DayXApp].
 */
@Composable
fun AppEntryPoint() {
    var showSplash by remember { mutableStateOf(true) }

    // Inicia o temporizador da splash ao entrar na composição
    LaunchedEffect(Unit) {
        delay(2600L)
        showSplash = false
    }

    // Alterna entre splash e tela principal
    if (showSplash) {
        SplashScreen()
    } else {
        DayXApp()
    }
}
