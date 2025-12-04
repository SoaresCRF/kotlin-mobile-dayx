package com.dev.soarescrf.dayx.data.repository

import com.dev.soarescrf.dayx.core.common.ResultState
import com.dev.soarescrf.dayx.data.local.database.HistoryDao
import com.dev.soarescrf.dayx.data.local.database.HistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório responsável pelo acesso aos dados locais armazenados no banco
 * de dados Room através do [HistoryDao].
 *
 * Todas as operações retornam um [ResultState], garantindo que erros sejam
 * tratados e comunicados de forma segura à camada superior da aplicação.
 *
 * @property dao DAO que fornece operações de leitura e escrita da tabela `history`.
 */
@Singleton
class HistoryDatabaseRepository @Inject constructor(
    private val dao: HistoryDao
) {

    /**
     * Obtém todos os registros armazenados localmente.
     *
     * Os dados são emitidos como um fluxo e mapeados para [ResultState],
     * permitindo tratamento de sucesso ou falha.
     *
     * @return Fluxo emitindo uma lista de [HistoryEntity] encapsulada em [ResultState].
     */
    fun getAll(): Flow<ResultState<List<HistoryEntity>>> =
        dao.getAll()
            .map<List<HistoryEntity>, ResultState<List<HistoryEntity>>> { list ->
                ResultState.Success(list)
            }
            .catch { throwable ->
                emit(
                    ResultState.Error(throwable.localizedMessage ?: "Erro ao carregar dados locais")
                )
            }


    /**
     * Insere múltiplos itens no banco de dados.
     *
     * Caso ocorra algum erro durante a operação, ele é capturado e convertido
     * em um [ResultState.Error].
     *
     * @param items Lista de entidades a serem inseridas.
     * @return [ResultState] indicando sucesso ou erro.
     */
    suspend fun insertAll(items: List<HistoryEntity>): ResultState<Unit> =
        runCatching {
            dao.insertAll(items)
        }.fold(
            onSuccess = {
                ResultState.Success(Unit)
            },
            onFailure = { throwable ->
                ResultState.Error(throwable.localizedMessage ?: "Erro ao inserir dados")
            }
        )


    /**
     * Remove todos os registros da tabela `history`.
     *
     * @return [ResultState] indicando se a operação foi concluída com sucesso ou erro.
     */
    suspend fun clearAll(): ResultState<Unit> =
        runCatching {
            dao.clearAll()
        }.fold(
            onSuccess = {
                ResultState.Success(Unit)
            },
            onFailure = { throwable ->
                ResultState.Error(throwable.localizedMessage ?: "Erro ao limpar dados")
            }
        )
}
