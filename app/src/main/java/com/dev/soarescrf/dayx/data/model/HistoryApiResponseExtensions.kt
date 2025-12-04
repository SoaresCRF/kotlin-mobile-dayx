package com.dev.soarescrf.dayx.data.model

/**
 * Conjunto de listas de palavras-chave utilizadas para identificar
 * possíveis inversões entre nascimentos e mortes retornadas pela API.
 *
 * A API pode, em alguns casos, inverter listas de Births e Deaths;
 * por isso estas palavras-chave ajudam a detectar inconsistências.
 */
private object Keywords {
    /** Palavras-chave comuns em descrições de morte. */
    val death = listOf(
        "died", "d.", "death",
        "killed", "murdered", "assassinated", "executed"
    )

    /** Palavras-chave comuns em descrições de nascimento. */
    val birth = listOf(
        "born", "b."
    )
}


/**
 * Verifica se a string contém pelo menos uma palavra-chave da lista informada.
 *
 * @param keywords Lista de termos a serem procurados.
 * @return `true` se algum termo for encontrado (ignorando maiúsculas/minúsculas).
 */
private fun String.containsAny(keywords: List<String>): Boolean =
    keywords.any { keyword ->
        contains(keyword, ignoreCase = true)
    }


/**
 * A API utilizada pode ocasionalmente inverter as listas de "Births" e "Deaths".
 *
 * Esta função detecta essa inconsistência avaliando as descrições textuais:
 * - Se muitos itens em *births* contêm palavras-chave de morte,
 * - e itens em *deaths* contêm palavras-chave de nascimento,
 * então assume-se que ocorreu uma inversão.
 *
 * Caso detectado, a função retorna uma nova instância com as listas invertidas.
 *
 * @return Uma instância corrigida de [HistoryApiResponse] ou a original.
 */
fun HistoryApiResponse.fixApiInversion(): HistoryApiResponse {
    val births = data.births
    val deaths = data.deaths

    val birthsLookLikeDeaths = births.any { entry ->
        entry.text.containsAny(Keywords.death)
    }
    val deathsLookLikeBirths = deaths.any { entry ->
        entry.text.containsAny(Keywords.birth)
    }

    return if (birthsLookLikeDeaths && deathsLookLikeBirths) {
        copy(data = data.copy(births = deaths, deaths = births))
    } else {
        this
    }
}
