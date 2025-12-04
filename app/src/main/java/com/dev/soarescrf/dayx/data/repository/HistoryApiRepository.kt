package com.dev.soarescrf.dayx.data.repository

import com.dev.soarescrf.dayx.core.common.ResultState
import com.dev.soarescrf.dayx.data.model.HistoryApiResponse
import com.dev.soarescrf.dayx.data.model.fixApiInversion
import com.dev.soarescrf.dayx.data.remote.HistoryApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório responsável por consumir a API remota de eventos históricos.
 *
 * Este repositório encapsula o tratamento de erros de rede e HTTP, garantindo
 * que a camada superior receba respostas padronizadas através de [ResultState].
 *
 * Além disso, aplica correções na resposta da API utilizando [fixApiInversion],
 * quando necessário.
 *
 * @property api Serviço Retrofit utilizado para fazer as requisições.
 */
@Singleton
class HistoryApiRepository @Inject constructor(
    private val api: HistoryApi
) {

    /**
     * Busca os eventos históricos referentes ao mês e dia informados.
     *
     * A resposta é encapsulada em um [ResultState] e pode representar:
     * - **Sucesso**, contendo um [HistoryApiResponse]
     * - **Erro**, contendo uma mensagem amigável
     *
     * Erros HTTP, exceções de rede (IO) e erros inesperados são convertidos
     * para mensagens descritivas.
     *
     * @param month Mês desejado (1–12).
     * @param day Dia do mês.
     * @return [ResultState] com o resultado da operação.
     */
    suspend fun fetchHistory(month: Int, day: Int): ResultState<HistoryApiResponse> =
        runCatching {
            api.getHistory(month, day)
        }.fold(
            onSuccess = { response ->
                if (!response.isSuccessful) {
                    return ResultState.Error(
                        httpErrorMessage(response.code(), response.message())
                    )
                }

                val body = response.body()
                    ?: return ResultState.Error("Resposta vazia do servidor")

                ResultState.Success(body.fixApiInversion())
            },
            onFailure = { throwable ->
                mapExceptionToState(throwable)
            }
        )


    /**
     * Converte exceções lançadas durante a requisição em um [ResultState.Error].
     *
     * @param t Exceção capturada.
     * @return Erro mapeado para uma mensagem clara e amigável ao usuário.
     */
    private fun mapExceptionToState(t: Throwable): ResultState.Error =
        when (t) {
            is IOException ->
                ResultState.Error("Falha na conexão. Verifique sua internet.")

            is HttpException ->
                ResultState.Error(httpErrorMessage(t.code(), t.message()))

            else ->
                ResultState.Error("Erro inesperado: ${t.message ?: "desconhecido"}")
        }


    /**
     * Mapeia códigos HTTP para mensagens amigáveis.
     *
     * @param code Código HTTP retornado.
     * @param message Mensagem opcional da exceção/response.
     * @return Mensagem legível representando o erro.
     */
    private fun httpErrorMessage(code: Int, message: String?): String =
        when (code) {
            400 -> "Requisição inválida (400)"
            401 -> "Não autorizado (401)"
            403 -> "Acesso negado (403)"
            404 -> "Recurso não encontrado (404)"
            408 -> "Timeout do servidor (408)"
            500 -> "Erro interno do servidor (500)"
            503 -> "Serviço indisponível (503)"
            else -> "Erro HTTP $code: ${message ?: "Erro desconhecido"}"
        }
}
