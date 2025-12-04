package com.dev.soarescrf.dayx.data.mappers

import com.dev.soarescrf.dayx.data.local.database.HistoryEntity
import com.dev.soarescrf.dayx.data.model.HistoryApiItem
import com.dev.soarescrf.dayx.data.model.HistoryApiResponse

/**
 * Converte a resposta da API [HistoryApiResponse] em uma lista de entidades [HistoryEntity]
 * prontas para serem armazenadas no banco local.
 *
 * A conversão agrega todas as listas internas (`births`, `deaths`, `events`)
 * adicionando uma categoria correspondente para cada tipo.
 *
 * @return Lista de entidades prontas para inserção no banco de dados.
 */
fun HistoryApiResponse.toEntityList(): List<HistoryEntity> =
    buildList {
        addAll(data.births.toEntities("births"))
        addAll(data.deaths.toEntities("deaths"))
        addAll(data.events.toEntities("events"))
    }


/**
 * Converte uma entidade [HistoryEntity] em um item de domínio [HistoryApiItem].
 *
 * Esta transformação é útil para exibir dados vindos do cache local
 * de forma consistente com os dados vindos da API.
 *
 * @return Objeto de domínio equivalente à entidade.
 */
fun HistoryEntity.toHistoryItem(): HistoryApiItem =
    HistoryApiItem(
        year = year,
        text = text
    )


/**
 * Converte uma lista de itens da API em uma lista de entidades [HistoryEntity],
 * atribuindo a categoria informada a todos os elementos.
 *
 * @param category Categoria à qual os itens pertencem (ex.: `"births"`, `"deaths"`, `"events"`).
 * @return Lista de entidades mapeadas e prontas para persistência no banco.
 */
private fun List<HistoryApiItem>.toEntities(category: String): List<HistoryEntity> =
    map { item ->
        HistoryEntity(
            year = item.year,
            text = item.text,
            category = category
        )
    }
