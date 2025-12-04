package com.dev.soarescrf.dayx.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Implementação do banco de dados local usando Room.
 *
 * Este banco armazena entidades relacionadas ao histórico, como datas,
 * eventos, nascimentos e mortes, através da tabela representada por [HistoryEntity].
 *
 * A versão do banco é definida como `2`. Ao alterar a estrutura das tabelas
 * (como adicionar, remover ou modificar colunas), é necessário atualizar esse valor
 * e implementar uma estratégia de migração ou usar `fallbackToDestructiveMigration`.
 *
 * @property historyDao DAO que fornece acesso às operações de leitura e escrita
 * na tabela `history`.
 */
@Database(
    entities = [HistoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class HistoryDatabase : RoomDatabase() {

    /**
     * Acesso ao DAO responsável pelas operações da tabela `history`.
     */
    abstract val historyDao: HistoryDao
}
