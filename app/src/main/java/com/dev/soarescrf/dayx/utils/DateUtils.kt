package com.dev.soarescrf.dayx.utils

import java.time.LocalDate

/**
 * Utilitário para operações relacionadas a datas dentro do aplicativo.
 *
 * Fornece métodos auxiliares que encapsulam chamadas frequentes
 * e facilitam a manutenção e testabilidade da camada de data.
 */
object DateUtils {

    /**
     * Obtém a data atual do sistema.
     *
     * @return Instância de [LocalDate] representando a data de hoje.
     */
    fun currentDate(): LocalDate {
        return LocalDate.now()
    }
}
