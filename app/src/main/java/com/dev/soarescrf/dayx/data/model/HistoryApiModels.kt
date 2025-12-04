package com.dev.soarescrf.dayx.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa a resposta principal retornada pela API de eventos históricos.
 *
 * @property date Data consultada na API.
 * @property data Conjunto de listas contendo nascimentos, mortes e eventos relevantes.
 */
data class HistoryApiResponse(
    val date: String,
    val data: HistoryApiData
)


/**
 * Estrutura que contém listas categoradas de itens históricos.
 *
 * A API retorna três categorias principais:
 * - **Births**: Nascimentos
 * - **Deaths**: Mortes
 * - **Events**: Eventos históricos gerais
 *
 * @property births Lista de ocorrências relacionadas a nascimentos.
 * @property deaths Lista de ocorrências relacionadas a mortes.
 * @property events Lista de acontecimentos históricos.
 */
data class HistoryApiData(
    @SerializedName("Births")
    val births: List<HistoryApiItem> = emptyList(),

    @SerializedName("Deaths")
    val deaths: List<HistoryApiItem> = emptyList(),

    @SerializedName("Events")
    val events: List<HistoryApiItem> = emptyList()
)


/**
 * Representa um item histórico retornado pela API.
 *
 * Cada item inclui o ano em que ocorreu, uma descrição textual
 * e uma lista de links informativos adicionais.
 *
 * @property year Ano do evento.
 * @property text Descrição do acontecimento.
 * @property links Lista de links relacionados ao evento.
 */
data class HistoryApiItem(
    val year: String,
    val text: String,
    val links: List<HistoryApiLink> = emptyList()
)


/**
 * Representa um link associado a um item histórico.
 *
 * @property title Título ou nome do link.
 * @property link URL apontando para uma fonte externa.
 */
data class HistoryApiLink(
    val title: String,
    val link: String
)
