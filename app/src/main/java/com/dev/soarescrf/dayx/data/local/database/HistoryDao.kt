package com.dev.soarescrf.dayx.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO responsável pelas operações de acesso aos dados da tabela `history`.
 *
 * Fornece métodos para consulta, inserção e remoção de registros armazenados
 * localmente no banco de dados Room.
 */
@Dao
interface HistoryDao {

    /**
     * Retorna todos os registros da tabela `history`.
     *
     * O uso de [Flow] permite observar mudanças em tempo real,
     * emitindo automaticamente novos valores quando a tabela é atualizada.
     *
     * @return Fluxo contendo a lista de entidades armazenadas no banco.
     */
    @Query("SELECT * FROM history")
    fun getAll(): Flow<List<HistoryEntity>>


    /**
     * Insere uma lista de registros no banco de dados.
     *
     * Caso já exista um item com a mesma chave primária, ele será substituído,
     * conforme a estratégia [OnConflictStrategy.REPLACE].
     *
     * @param items Lista de entidades a serem inseridas.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HistoryEntity>)


    /**
     * Remove todos os registros da tabela `history`.
     *
     * Esta operação limpa completamente o armazenamento local.
     */
    @Query("DELETE FROM history")
    suspend fun clearAll()
}
