package com.dev.soarescrf.dayx.data.remote

import com.dev.soarescrf.dayx.data.model.HistoryApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface responsável pelas chamadas à API de eventos históricos.
 *
 * Esta API permite consultar acontecimentos que ocorreram em uma data específica,
 * retornando listas de nascimentos, mortes e eventos marcantes.
 */
interface HistoryApi {

    /**
     * Obtém os eventos históricos referentes ao dia e mês informados.
     *
     * A rota utiliza o formato:
     * ```
     * GET date/{month}/{day}
     * ```
     *
     * @param month Mês desejado (1 a 12).
     * @param day Dia do mês.
     * @return [Response] contendo um [HistoryApiResponse] com os dados retornados pela API.
     */
    @GET("date/{month}/{day}")
    suspend fun getHistory(
        @Path("month") month: Int,
        @Path("day") day: Int
    ): Response<HistoryApiResponse>
}
