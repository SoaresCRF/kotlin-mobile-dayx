package com.dev.soarescrf.dayx.core.common

import com.dev.soarescrf.dayx.data.model.HistoryApiItem

/**
 * Representa o estado da interface (UI) durante operações de carregamento
 * ou obtenção de dados relacionados a itens históricos.
 *
 * Esta classe selada é utilizada para controlar a renderização da tela,
 * indicando se a aplicação está carregando, exibindo dados ou apresentando um erro.
 */
sealed class UiState {

    /**
     * Estado emitido enquanto os dados estão sendo carregados.
     */
    data object Loading : UiState()


    /**
     * Estado emitido quando os dados são carregados com sucesso.
     *
     * @property itemsByCategory Mapa contendo listas de itens históricos,
     * agrupados por categoria [HistoryCategory].
     */
    data class Success(
        val itemsByCategory: Map<HistoryCategory, List<HistoryApiItem>>
    ) : UiState()


    /**
     * Estado emitido quando ocorre um erro ao obter os dados.
     *
     * @property message Mensagem descrevendo a causa do erro.
     */
    data class Error(
        val message: String
    ) : UiState()
}


/**
 * Representa o estado de uma operação que pode resultar em sucesso ou erro.
 *
 * Essa classe selada é geralmente usada em chamadas de repositório, casos de uso
 * ou operações assíncronas para encapsular o resultado de forma segura.
 *
 * @param T Tipo do dado retornado em caso de sucesso.
 */
sealed class ResultState<out T> {

    /**
     * Estado que representa uma operação concluída com sucesso.
     *
     * @param data Dados retornados pela operação.
     */
    data class Success<T>(val data: T) : ResultState<T>()


    /**
     * Estado que representa uma falha durante a operação.
     *
     * @param message Mensagem descrevendo o erro ocorrido.
     */
    data class Error(val message: String) : ResultState<Nothing>()
}
