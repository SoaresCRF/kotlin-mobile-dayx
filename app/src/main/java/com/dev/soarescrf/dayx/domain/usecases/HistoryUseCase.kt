package com.dev.soarescrf.dayx.domain.usecases

import com.dev.soarescrf.dayx.core.common.ResultState
import com.dev.soarescrf.dayx.data.local.database.HistoryEntity
import com.dev.soarescrf.dayx.data.mappers.toEntityList
import com.dev.soarescrf.dayx.data.repository.AppPreferencesRepository
import com.dev.soarescrf.dayx.data.repository.HistoryApiRepository
import com.dev.soarescrf.dayx.data.repository.HistoryDatabaseRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Caso de uso responsável por orquestrar a busca de eventos históricos,
 * aplicando lógica de cache, controle de atualizações e integração entre:
 *
 * - API remota ([HistoryApiRepository])
 * - Banco de dados local ([HistoryDatabaseRepository])
 * - Preferências/estado local ([AppPreferencesRepository])
 *
 * A lógica principal determina se os dados do dia solicitado podem ser
 * reutilizados a partir do cache local ou se é necessário consultar a API.
 */
@Singleton
class HistoryUseCase @Inject constructor(
    private val apiRepository: HistoryApiRepository,
    private val databaseRepository: HistoryDatabaseRepository,
    private val preferencesRepository: AppPreferencesRepository
) {

    /**
     * Obtém os eventos históricos referentes à data informada.
     *
     * Fluxo de funcionamento:
     * 1. Verifica se existe cache válido para a data.
     * 2. Se válido, carrega dados locais.
     * 3. Caso contrário, busca da API, salva no banco e atualiza o cache.
     *
     * @param date Data desejada.
     * @return [ResultState] contendo a lista de [HistoryEntity] ou erro.
     */
    suspend fun getHistory(date: LocalDate): ResultState<List<HistoryEntity>> {
        val (month, day) = date.run { monthValue to dayOfMonth }

        if (isCacheValidFor(date)) {
            loadLocalHistory()?.let { result ->
                return result
            }
        }

        return fetchRemoteHistoryAndCache(month, day, date)
    }


    /**
     * Verifica se o cache está válido para a data solicitada.
     *
     * O cache é considerado válido se a última atualização registrada
     * nas preferências for exatamente a mesma data solicitada.
     *
     * @param date Data desejada.
     * @return `true` se o cache é válido, caso contrário `false`.
     */
    private suspend fun isCacheValidFor(date: LocalDate): Boolean {
        val lastUpdatedDate = preferencesRepository.getLastUpdate().firstOrNull()
        return lastUpdatedDate is ResultState.Success && lastUpdatedDate.data == date
    }


    /**
     * Tenta carregar os dados armazenados localmente.
     *
     * Só retorna sucesso se a lista estiver preenchida; caso contrário,
     * sinaliza que é necessário buscar dados remotos.
     *
     * @return Resultado de sucesso contendo dados locais ou `null`.
     */
    private suspend fun loadLocalHistory(): ResultState<List<HistoryEntity>>? {
        val databaseResult = databaseRepository.getAll().firstOrNull()

        if (databaseResult is ResultState.Success && databaseResult.data.isNotEmpty()) {
            return ResultState.Success(databaseResult.data)
        }

        return null
    }


    /**
     * Realiza a chamada remota para a API e atualiza o cache local.
     *
     * Se ocorrer erro ao fazer a requisição, o erro é retornado diretamente.
     *
     * @param month Mês solicitado.
     * @param day Dia solicitado.
     * @param date Data completa usada na atualização do cache.
     *
     * @return [ResultState] contendo dados convertidos para entidades ou erro.
     */
    private suspend fun fetchRemoteHistoryAndCache(
        month: Int,
        day: Int,
        date: LocalDate
    ): ResultState<List<HistoryEntity>> {

        return when (val apiResult = apiRepository.fetchHistory(month, day)) {

            is ResultState.Error ->
                ResultState.Error(apiResult.message)

            is ResultState.Success -> {
                val historyEntities = apiResult.data.toEntityList()

                refreshCache(historyEntities, date)

                ResultState.Success(historyEntities)
            }
        }
    }


    /**
     * Atualiza completamente o cache local, substituindo todos os dados:
     *
     * 1. Limpa os registros do banco.
     * 2. Insere os novos registros.
     * 3. Salva a data de atualização nas preferências.
     *
     * @param historyEntities Dados a serem armazenados.
     * @param date Data utilizada para validar o cache.
     */
    private suspend fun refreshCache(
        historyEntities: List<HistoryEntity>,
        date: LocalDate
    ) {
        databaseRepository.clearAll()
        databaseRepository.insertAll(historyEntities)
        preferencesRepository.saveLastUpdate(date)
    }
}
