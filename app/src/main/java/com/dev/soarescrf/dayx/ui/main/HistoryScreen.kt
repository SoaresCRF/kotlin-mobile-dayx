package com.dev.soarescrf.dayx.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.soarescrf.dayx.core.common.HistoryCategory
import com.dev.soarescrf.dayx.core.common.UiState

/**
 * Tela responsável por exibir a lista de eventos históricos de uma categoria específica.
 *
 * A UI reage ao estado exposto pelo [MainViewModel] através de [UiState]:
 * - **Loading** → Exibe a tela de carregamento.
 * - **Error** → Exibe uma mensagem de erro.
 * - **Success** → Renderiza a lista filtrada pela categoria solicitada.
 *
 * @param category Categoria dos eventos a serem exibidos (Births, Deaths, Events).
 * @param viewModel ViewModel injetado via Hilt, responsável pela lógica de carregamento.
 */
@Composable
fun HistoryScreen(
    category: HistoryCategory,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        UiState.Loading ->
            LoadingView()

        is UiState.Error ->
            ErrorView(state.message)

        is UiState.Success ->
            HistoryList(state.itemsByCategory[category] ?: emptyList())
    }
}
