package com.dev.soarescrf.dayx.data.repository

import com.dev.soarescrf.dayx.core.common.ResultState
import com.dev.soarescrf.dayx.data.local.datastore.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório responsável por encapsular o acesso às preferências da aplicação,
 * utilizando [AppPreferences] como fonte de dados.
 *
 * Este repositório expõe operações seguras através de [ResultState],
 * garantindo que exceções sejam capturadas e tratadas adequadamente.
 *
 * @property prefs Instância de [AppPreferences] fornecida via injeção de dependência.
 */
@Singleton
class AppPreferencesRepository @Inject constructor(
    private val prefs: AppPreferences
) {

    /**
     * Obtém a última data de atualização armazenada, emitida como um fluxo reativo.
     *
     * O fluxo é transformado em [ResultState], encapsulando tanto o valor de sucesso
     * quanto possíveis erros que ocorram durante a leitura.
     *
     * @return Fluxo emitindo um [ResultState] contendo a última data ou um erro.
     */
    fun getLastUpdate(): Flow<ResultState<LocalDate?>> =
        prefs.lastUpdate
            .map { date ->
                runCatching {
                    date
                }.fold(
                    onSuccess = { dateValue ->
                        ResultState.Success(dateValue)
                    },
                    onFailure = { throwable ->
                        ResultState.Error(throwable.localizedMessage ?: "Erro ao ler a última atualização")
                    }
                )
            }


    /**
     * Salva a última data de atualização nas preferências.
     *
     * @param date Data a ser registrada.
     * @return [ResultState] indicando sucesso ou falha na operação.
     */
    suspend fun saveLastUpdate(date: LocalDate): ResultState<Unit> =
        runCatching {
            prefs.saveLastUpdate(date)
        }.fold(
            onSuccess = {
                ResultState.Success(Unit)
            },
            onFailure = { throwable ->
                ResultState.Error(
                    throwable.localizedMessage ?: "Erro ao salvar a última atualização"
                )
            }
        )


    /**
     * Limpa todas as preferências persistidas.
     *
     * @return [ResultState] indicando sucesso ou falha na operação.
     */
    suspend fun clear(): ResultState<Unit> =
        runCatching {
            prefs.clear()
        }.fold(
            onSuccess = {
                ResultState.Success(Unit)
            },
            onFailure = { throwable ->
                ResultState.Error(throwable.localizedMessage ?: "Erro ao limpar as preferências")
            }
        )
}
