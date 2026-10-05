package com.example.app_rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_rpg.ui.theme.App_rpgTheme
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corCinzaEscuro
import com.example.app_rpg.ui.theme.corCinzaMedio
import com.example.app_rpg.ui.theme.corJogadorPrincipal
import com.example.app_rpg.ui.theme.corMestreDestaque
import com.example.app_rpg.ui.theme.corPretoPuro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_rpgTheme {
                App()
            }
        }
    }
}

object Routes {
    const val DICE = "dice"
    const val CHARACTER_SHEET = "character_sheet"
    const val PLAYLIST = "playlist"
    const val ITEMS = "items"
    const val STORE_LIST = "stores"

    const val STORE_ID_ARG = "storeId"
    const val STORE = "store/{$STORE_ID_ARG}"
    const val STORE_REGISTRATION = "store_registration?$STORE_ID_ARG={$STORE_ID_ARG}"

    fun store(storeId: Int) = "store/$storeId"
    fun storeRegistration(storeId: Int? = null) =
        if (storeId == null) "store_registration" else "store_registration?$STORE_ID_ARG=$storeId"
}

enum class Destination(
    val label: String,
    @param:DrawableRes val iconRes: Int,
    val route: String
) {
    Dice("Dados", R.drawable.ic_dice, Routes.DICE),
    CharacterSheet("Ficha", R.drawable.ic_character_sheet, Routes.CHARACTER_SHEET),
    Playlist("Música", R.drawable.ic_playlist, Routes.PLAYLIST),
    Items("Itens", R.drawable.icon_tools, Routes.ITEMS),
    StoreList("Lojas", R.drawable.ic_store, Routes.STORE_LIST)
}

private val storeInternalRoutes = setOf(Routes.STORE, Routes.STORE_REGISTRATION)

enum class Mode(
    val label: String,
    val tabs: List<Destination>
) {
    Player("Jogador", listOf(Destination.Dice, Destination.CharacterSheet, Destination.StoreList)),
    Master("Mestre", listOf(Destination.Playlist, Destination.Items, Destination.StoreList))
}

private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var user by rememberSaveable { mutableStateOf(User()) }
    val mode = user.mode
    val lojas = remember {
        mutableStateListOf<LojaRpg>()
    }

    val itens = remember {
        mutableStateListOf<ItemRpg>()
    }

    val routeStoreId = backStackEntry?.arguments?.getInt(Routes.STORE_ID_ARG)
    val routeStore = lojas.firstOrNull { it.id == routeStoreId }
    val tab = Destination.entries.firstOrNull { it.route == currentRoute }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = corPretoPuro,
        topBar = {
            AppTopBar(
                title = when (currentRoute) {
                    Routes.STORE -> routeStore?.nome ?: "Loja"
                    Routes.STORE_REGISTRATION -> if (routeStore == null) "Criar Loja" else "Editar Loja"
                    else -> tab?.label.orEmpty()
                },
                mode = mode,
                subtitle = if (currentRoute == Routes.STORE) {
                    if (itens.size == 1) "1 item à venda" else "${itens.size} itens à venda"
                } else {
                    null
                },
                onBack = if (tab == null) {
                    { navController.popBackStack() }
                } else {
                    null
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = corCinzaEscuro) {
                mode.tabs
                    .forEach { destination ->
                        NavigationBarItem(
                            selected = currentRoute == destination.route ||
                                (destination == Destination.StoreList && currentRoute in storeInternalRoutes),

                            onClick = {
                                navController.navigateToTab(destination.route)
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        destination.iconRes
                                    ),
                                    contentDescription = destination.label
                                )
                            },
                            label = {
                                Text(
                                    text = destination.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors =
                                NavigationBarItemDefaults.colors(
                                    selectedIconColor = corBrancoPuro,
                                    selectedTextColor = corBrancoPuro,

                                    indicatorColor = when (mode) {
                                        Mode.Master -> corMestreDestaque
                                        Mode.Player -> corJogadorPrincipal
                                },
                                unselectedIconColor = corCinzaMedio,
                                unselectedTextColor = corCinzaMedio,
                            )
                    )
                }

                //Toggles between Player and Master mode
                val modeColor = when (mode) {
                    Mode.Master -> corMestreDestaque
                    Mode.Player -> corJogadorPrincipal
                }
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        val newMode = when (mode) {
                            Mode.Player -> Mode.Master
                            Mode.Master -> Mode.Player
                        }
                        user = user.copy(mode = newMode)
                        val stillAvailable = newMode.tabs.any { it.route == currentRoute } ||
                            currentRoute in storeInternalRoutes
                        if (!stillAvailable) {
                            navController.navigateToTab(newMode.tabs.first().route)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_swap),
                            contentDescription = "Alternar para modo ${
                                if (mode == Mode.Player) Mode.Master.label else Mode.Player.label
                            }"
                        )
                    },
                    label = {
                        Text(
                            text = mode.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = modeColor,
                        unselectedTextColor = modeColor,
                    )
                )
        }
    }
) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        NavHost(
            navController = navController,
            startDestination = Routes.DICE
        ) {
            composable(Routes.DICE) {
                DiceScreen(contentModifier)
            }

            composable(Routes.CHARACTER_SHEET) {
                FichaPersonagemScreen(contentModifier)
            }

            composable(Routes.PLAYLIST) {
                Playlist(contentModifier)
            }

            composable(Routes.ITEMS) {
                ItemRegistrationScreen(
                    contentModifier,
                    itens
                )
            }

            composable(Routes.STORE_LIST) {
                StoreListScreen(
                    modifier = contentModifier,
                    lojas = lojas,
                    isMaster = mode == Mode.Master,
                    onLojaClick = { loja ->
                        navController.navigate(Routes.store(loja.id))
                    },
                    onAdicionar = {
                        navController.navigate(Routes.storeRegistration())
                    },
                    onEditar = { loja ->
                        navController.navigate(Routes.storeRegistration(loja.id))
                    },
                    onExcluir = { loja ->
                        lojas.removeAll { it.id == loja.id }
                    },
                    onToggleVisibility = { loja ->
                        val indice = lojas.indexOfFirst { it.id == loja.id }
                        if (indice >= 0) {
                            lojas[indice] = loja.copy(visivel = !loja.visivel)
                        }
                    }
                )
            }

            composable(
                route = Routes.STORE,
                arguments = listOf(navArgument(Routes.STORE_ID_ARG) { type = NavType.IntType })
            ) {
                StoreScreen(
                    modifier = contentModifier,
                    itens = itens
                )
            }

            composable(
                route = Routes.STORE_REGISTRATION,
                arguments = listOf(
                    navArgument(Routes.STORE_ID_ARG) {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { entry ->
                val storeId = entry.arguments?.getInt(Routes.STORE_ID_ARG)
                val editingStore = lojas.firstOrNull { it.id == storeId }

                StoreRegistrationScreen(
                    modifier = contentModifier,
                    lojaInicial = editingStore,
                    onCancelar = {
                        navController.popBackStack()
                    },

                    onSalvar = { loja ->
                        val indice = lojas.indexOfFirst { it.id == loja.id }

                        if (indice >= 0) {
                            lojas[indice] = loja
                        } else {
                            val novaLoja = loja.copy(
                                id = (lojas.maxOfOrNull { it.id } ?: 0) + 1
                            )

                            lojas.add(novaLoja)
                        }

                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(title, color = corCinzaMedio, fontSize = 18.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppPreview() {
    App_rpgTheme {
        App()
    }
}
