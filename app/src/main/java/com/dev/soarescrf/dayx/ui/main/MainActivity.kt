package com.dev.soarescrf.dayx.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dev.soarescrf.dayx.ui.AppEntryPoint
import com.dev.soarescrf.dayx.ui.theme.DayXTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal do aplicativo, responsável por inicializar a interface
 * baseada em Jetpack Compose e configurar a injeção de dependências via Hilt.
 *
 * A Activity:
 * - Habilita o modo edge-to-edge para uma experiência mais imersiva.
 * - Define o conteúdo da tela utilizando o tema [DayXTheme].
 * - Renderiza o composable raiz [AppEntryPoint], que representa o ponto
 *   de entrada principal da UI.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DayXTheme {
                AppEntryPoint()
            }
        }
    }
}
