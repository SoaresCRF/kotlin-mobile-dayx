package com.dev.soarescrf.dayx.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.soarescrf.dayx.core.common.HistoryCategory
import com.dev.soarescrf.dayx.core.common.ResultState
import com.dev.soarescrf.dayx.core.common.UiState
import com.dev.soarescrf.dayx.data.local.database.HistoryEntity
import com.dev.soarescrf.dayx.data.mappers.toHistoryItem
import com.dev.soarescrf.dayx.data.model.HistoryApiItem
import com.dev.soarescrf.dayx.domain.usecases.HistoryUseCase
import com.dev.soarescrf.dayx.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel responsável por coordenar a lógica de carregamento dos dados históricos
 * e expor o estado da UI através de [UiState].
 *
 * A ViewModel interage com o [HistoryUseCase] para:
 * - Buscar dados remotos ou do cache;
 * - Tratar erros de forma padronizada;
 * - Agrupar resultados por categoria para exibição na UI.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val historyUseCase: HistoryUseCase
) : ViewModel() {

    /** Estado interno mutável da UI. */
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)

    /** Estado exposto imutável para observação pela UI. */
    val uiState: StateFlow<UiState> = _uiState


    init {
        // Carrega automaticamente o histórico da data atual
        refresh()
    }


    /**
     * Atualiza as informações de eventos históricos utilizando a data atual do sistema.
     */
    fun refresh() = getHistory(DateUtils.currentDate())


    /**
     * Obtém os eventos históricos de uma data específica.
     *
     * O fluxo consiste em:
     * 1. Emitir estado de carregamento.
     * 2. Executar o caso de uso.
     * 3. Converter e agrupar os resultados.
     * 4. Atualizar o [UiState] conforme sucesso ou erro.
     *
     * @param date Data desejada.
     */
    private fun getHistory(date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when (val result = historyUseCase.getHistory(date)) {
                is ResultState.Success -> {
                    val grouped = result.data.groupByCategory()
                    _uiState.value = UiState.Success(grouped)
                }

                is ResultState.Error -> {
                    _uiState.value = UiState.Error(result.message)
                }
            }
        }
    }


    /**
     * Agrupa a lista de entidades [HistoryEntity] com base na categoria correspondente.
     *
     * Para cada categoria de [HistoryCategory], a função filtra apenas os registros
     * que pertencem àquela categoria e os transforma em [HistoryApiItem].
     *
     * @return Mapa contendo listas de itens por categoria.
     */
    private fun List<HistoryEntity>.groupByCategory(): Map<HistoryCategory, List<HistoryApiItem>> =
        HistoryCategory.entries.associateWith { category ->
            filter { entity ->
                entity.category == category.route
            }.map { entity ->
                entity.toHistoryItem()
            }
        }
}
