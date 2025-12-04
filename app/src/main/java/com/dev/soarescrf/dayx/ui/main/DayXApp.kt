package com.dev.soarescrf.dayx.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.soarescrf.dayx.R
import com.dev.soarescrf.dayx.core.common.HistoryCategory

/**
 * Composable principal do aplicativo.
 *
 * Responsável por configurar:
 * - Navegação principal com NavigationSuiteScaffold (adaptive navigation);
 * - O NavController;
 * - A renderização das três telas: Births, Deaths e Events;
 * - Ícones, labels e comportamento de navegação.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayXApp() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentDestination = AppDestination.entries.find { entry ->
        entry.route == currentRoute
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestination.entries.forEach { destination ->
                val isSelected = currentRoute == destination.route

                item(
                    icon = { DestinationIcon(destination, isSelected) },
                    label = { DestinationLabel(destination, isSelected) },
                    selected = isSelected,
                    onClick = { navController.navigateSingleTopTo(destination.route) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(currentDestination)
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = AppDestination.Births.route,
                modifier = Modifier.padding(padding)
            ) {
                AppDestination.entries.forEach { destination ->
                    composable(destination.route) {
                        HistoryScreen(category = destination.category)
                    }
                }
            }
        }
    }
}


/**
 * Ícone de cada destino da navegação.
 *
 * A cor muda conforme o item está selecionado ou não.
 *
 * @param dest Destino representado.
 * @param selected Indica se este destino está ativo na navegação.
 */
@Composable
private fun DestinationIcon(dest: AppDestination, selected: Boolean) {
    Icon(
        painter = painterResource(dest.iconRes),
        contentDescription = dest.label,
        tint = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified
    )
}


/**
 * Label exibido abaixo ou ao lado do ícone dependendo do layout adaptativo.
 *
 * @param dest Destino atual.
 * @param selected Indica se o destino está selecionado.
 */
@Composable
private fun DestinationLabel(dest: AppDestination, selected: Boolean) {
    Text(
        text = dest.label,
        color = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified,
        style = MaterialTheme.typography.labelSmall
    )
}


/**
 * Navega para um destino garantindo:
 * - Evita múltiplas cópias do mesmo destino na pilha (SingleTop);
 * - Restaura estado quando possível;
 * - Remove rotas intermediárias, mantendo somente o startDestination.
 *
 * @param route Rota para navegar.
 */
private fun NavController.navigateSingleTopTo(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}


/**
 * Barra superior (TopAppBar) da aplicação.
 *
 * Exibe o título associado ao destino de navegação atual,
 * quando um [AppDestination] válido é fornecido.
 *
 * @param destination Destino atual da navegação. Caso seja `null`,
 *                    nenhuma barra superior é exibida.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(destination: AppDestination?) {
    if (destination != null) {
        TopAppBar(
            title = {
                Text(
                    text = destination.title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        )
    }
}


/**
 * Classe selada que representa cada destino da navegação principal.
 *
 * @property route rota usada pelo NavController
 * @property label rótulo curto usado em itens de navegação
 * @property title título exibido na TopAppBar
 * @property iconRes ícone representativo do destino
 * @property category categoria histórica associada, usada na tela [HistoryScreen]
 */
sealed class AppDestination(
    val route: String,
    val label: String,
    val title: String,
    val iconRes: Int,
    val category: HistoryCategory
) {

    /** Destino que exibe itens da categoria *Births*. */
    object Births : AppDestination(
        route = HistoryCategory.BIRTH.route,
        label = "Births",
        title = "Aniversariantes históricos deste dia",
        iconRes = R.drawable.ic_birth,
        category = HistoryCategory.BIRTH
    )

    /** Destino que exibe itens da categoria *Deaths*. */
    object Deaths : AppDestination(
        route = HistoryCategory.DEATH.route,
        label = "Deaths",
        title = "Despedidas marcantes nesta data",
        iconRes = R.drawable.ic_death,
        category = HistoryCategory.DEATH
    )

    /** Destino que exibe itens da categoria *Events*. */
    object Events : AppDestination(
        route = HistoryCategory.EVENT.route,
        label = "Events",
        title = "Fatos que moldaram esta data",
        iconRes = R.drawable.ic_star,
        category = HistoryCategory.EVENT
    )

    companion object {
        /** Lista completa dos destinos disponíveis no app. */
        val entries = listOf(Births, Deaths, Events)
    }
}
