package com.dev.soarescrf.dayx.data.local.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo responsável por fornecer instâncias do banco de dados local e seus DAOs.
 *
 * Este módulo utiliza o Hilt para injeção de dependências,
 * garantindo que as instâncias do banco sejam únicas e adequadamente
 * gerenciadas ao longo do ciclo de vida da aplicação.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Fornece a instância única (*singleton*) do banco de dados [HistoryDatabase].
     *
     * @param context Contexto da aplicação, utilizado para inicializar o banco.
     * @return Instância configurada do banco de dados Room.
     *
     * O método usa `fallbackToDestructiveMigration(true)` para
     * recriar o banco automaticamente caso haja incompatibilidades de migração.
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): HistoryDatabase {
        return Room.databaseBuilder(
            context,
            HistoryDatabase::class.java,
            "history_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }


    /**
     * Fornece a instância do [HistoryDao], obtida a partir do banco de dados.
     *
     * @param database Instância do banco fornecida pelo Hilt.
     * @return Instância do DAO responsável pelas operações relacionadas ao banco de dados.
     */
    @Provides
    @Singleton
    fun provideHistoryDao(
        database: HistoryDatabase
    ): HistoryDao = database.historyDao
}
