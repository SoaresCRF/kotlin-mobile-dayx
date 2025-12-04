package com.dev.soarescrf.dayx.core.common

/**
 * Representa as categorias possíveis de registros históricos.
 *
 * Cada categoria está associada a uma rota utilizada para
 * navegação ou identificação em APIs e componentes da aplicação.
 *
 * @property route Rota correspondente à categoria, usada para
 * mapear eventos históricos como nascimentos, mortes e eventos gerais.
 */
enum class HistoryCategory(val route: String) {

    /** Categoria que representa registros de **nascimentos** históricos. */
    BIRTH("births"),

    /** Categoria que representa registros de **mortes** históricas. */
    DEATH("deaths"),

    /** Categoria que representa **eventos marcantes** na história. */
    EVENT("events")
}
