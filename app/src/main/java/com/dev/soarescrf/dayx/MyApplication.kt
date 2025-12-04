package com.dev.soarescrf.dayx

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Classe de aplicação do projeto, responsável por inicializar
 * o Hilt para injeção de dependências em todo o app.
 *
 * A anotação [HiltAndroidApp] gera os componentes base necessários
 * e configura automaticamente o ciclo de vida da injeção.
 */
@HiltAndroidApp
class MyApplication : Application()
