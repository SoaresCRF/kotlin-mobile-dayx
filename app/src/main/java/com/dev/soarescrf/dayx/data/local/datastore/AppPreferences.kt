package com.dev.soarescrf.dayx.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Extensão que cria e disponibiliza o DataStore de preferências da aplicação.
 *
 * O arquivo de preferências é armazenado com o nome `"app_prefs"`.
 */
private val Context.dataStore: androidx.datastore.core.DataStore<Preferences>
        by preferencesDataStore(name = "app_prefs")


/**
 * Classe responsável por gerenciar as preferências da aplicação utilizando o DataStore.
 *
 * Atualmente, esta classe armazena a data da última atualização local,
 * permitindo que a aplicação saiba quando os dados armazenados foram sincronizados pela última vez.
 *
 * @property context Contexto da aplicação injetado via Hilt.
 */
class AppPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private companion object {
        /** Chave utilizada para armazenar a data da última atualização. */
        val KEY_LAST_UPDATE = stringPreferencesKey("last_update")

        /** Formatador utilizado para conversão entre `LocalDate` e `String`. */
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }

    /**
     * Fluxo que emite a data da última atualização dos dados.
     *
     * Se o valor não estiver salvo, o fluxo emitirá `null`.
     *
     * @return Flow contendo a data da última atualização ou `null`.
     */
    val lastUpdate: Flow<LocalDate?> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_LAST_UPDATE]?.let { date ->
                LocalDate.parse(date, DATE_FORMATTER)
            }
        }


    /**
     * Salva a data da última atualização no DataStore.
     *
     * @param date Data a ser armazenada.
     */
    suspend fun saveLastUpdate(date: LocalDate) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_UPDATE] = date.format(DATE_FORMATTER)
        }
    }


    /**
     * Limpa todas as preferências armazenadas no DataStore.
     *
     * Essa operação remove todas as chaves e valores existentes.
     */
    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
