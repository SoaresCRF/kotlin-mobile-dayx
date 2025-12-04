package com.dev.soarescrf.dayx.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade que representa um item histórico armazenado localmente na tabela `history`.
 *
 * Cada registro contém informações sobre um acontecimento específico,
 * incluindo o ano, a descrição e a categoria associada.
 *
 * @property id Identificador único gerado automaticamente pelo Room.
 * @property year Ano em que o evento ocorreu, representado como texto.
 * @property text Descrição detalhada do evento.
 * @property category Categoria do evento (ex.: nascimento, morte, evento),
 * geralmente associada à enum [com.dev.soarescrf.dayx.core.common.HistoryCategory].
 */
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val year: String,

    val text: String,

    val category: String
)
