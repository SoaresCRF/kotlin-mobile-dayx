package com.dev.soarescrf.dayx.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Módulo Hilt responsável por configurar e fornecer as dependências de rede,
 * incluindo Retrofit, OkHttp e Gson.
 *
 * Este módulo garante instâncias únicas (*singleton*) dos componentes essenciais
 * para comunicação com a API remota.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /** URL base da API de eventos históricos. */
    private const val BASE_URL = "https://history.muffinlabs.com/"


    /** Tempo limite padrão (em segundos) para requisições HTTP. */
    private const val TIMEOUT_SECONDS = 30L


    /**
     * Fornece uma instância configurada de [Gson].
     *
     * @return Objeto Gson utilizado na serialização e desserialização JSON.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .create()


    /**
     * Configura e fornece uma instância única de [OkHttpClient].
     *
     * Define tempos limite de conexão, leitura e escrita para evitar requisições pendentes.
     *
     * @return Cliente HTTP configurado para uso no Retrofit.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()


    /**
     * Fornece e configura uma instância única de [Retrofit].
     *
     * @param gson Instância de Gson usada no conversor JSON.
     * @param client Cliente HTTP configurado.
     * @return Retrofit pronto para criar serviços de API.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


    /**
     * Cria e fornece a implementação da interface [HistoryApi].
     *
     * @param retrofit Instância do Retrofit configurada.
     * @return Serviço REST para consumir a API de eventos históricos.
     */
    @Provides
    @Singleton
    fun provideHistoryApi(retrofit: Retrofit): HistoryApi =
        retrofit.create(HistoryApi::class.java)
}
