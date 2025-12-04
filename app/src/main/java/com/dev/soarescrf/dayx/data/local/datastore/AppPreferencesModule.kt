package com.dev.soarescrf.dayx.data.local.datastore

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt responsável por fornecer a instância de [AppPreferences].
 *
 * Este módulo garante que a classe de gerenciamento do DataStore seja criada
 * apenas uma vez durante todo o ciclo de vida da aplicação.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppPreferencesModule {

    /**
     * Fornece uma instância única (*singleton*) de [AppPreferences].
     *
     * @param context Contexto da aplicação, necessário para acessar o DataStore.
     * @return Instância configurada de [AppPreferences].
     */
    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context
    ): AppPreferences = AppPreferences(context)
}
