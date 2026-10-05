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

enum class Destination(
    val label: String,
    @param:DrawableRes val iconRes: Int
) {
    Dice("Dados", R.drawable.ic_dice),
    CharacterSheet("Ficha", R.drawable.ic_character_sheet),
    Playlist("Música", R.drawable.ic_playlist),

    Items("Itens", R.drawable.icon_tools),
    StoreList("Lojas", R.drawable.ic_store),

    //Telas internas, não aparecem na barra inferior
    StoreRegistration("Criar Loja", R.drawable.ic_store),
    Store("Loja", R.drawable.ic_store)
}

private val telasInternasLojas = setOf(Destination.StoreRegistration, Destination.Store)

@Composable
fun App() {
    var current by rememberSaveable { mutableStateOf(Destination.Dice) }
    val lojas = remember {
        mutableStateListOf<LojaRpg>()
    }
    var nomeLoja by rememberSaveable {
        mutableStateOf("")
    }

    //Loja sendo editada; null quando o cadastro é de uma loja nova
    var lojaEditando by remember {
        mutableStateOf<LojaRpg?>(null)
    }

    val itens = remember {
        mutableStateListOf<ItemRpg>()
    }

    //   Destination.StoreRegistration -> StoreRegistrationScreen(
    //       modifier = contentModifier,
    //       onLojaSalva = salvarNomeLoja
    //   )

    val salvarNomeLoja: (String) -> Unit = { novoNome ->
        nomeLoja = novoNome.trim()
        current = Destination.Store
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = corPretoPuro,
        bottomBar = {
            NavigationBar(containerColor = corCinzaEscuro) {
                Destination.entries
                    .filter {
                        it !in telasInternasLojas
                    }
                    .forEach { destination ->
                        NavigationBarItem(
                            selected = current == destination ||
                                (destination == Destination.StoreList && current in telasInternasLojas),

                            onClick = {
                                current = destination
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

                                    indicatorColor = when(destination){
                                        Destination.Playlist,
                                        Destination.Items ->
                                            corMestreDestaque
                                        else ->
                                            corJogadorPrincipal
                                },
                                unselectedIconColor = corCinzaMedio,
                                unselectedTextColor = corCinzaMedio,
                            )
                    )
                }
        }
    }
) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (current) {
            Destination.Dice -> DiceScreen(contentModifier)
            Destination.CharacterSheet -> FichaPersonagemScreen(contentModifier)
            Destination.Playlist -> Playlist(contentModifier)
            Destination.Items -> ItemRegistrationScreen(
                contentModifier,
                itens
            )
            Destination.StoreList -> StoreListScreen(
                modifier = contentModifier,
                lojas = lojas,
                onLojaClick = { loja ->
                    nomeLoja = loja.nome
                    current = Destination.Store
                },
                onAdicionar = {
                    lojaEditando = null
                    current = Destination.StoreRegistration
                },
                onEditar = { loja ->
                    lojaEditando = loja
                    current = Destination.StoreRegistration
                },
                onExcluir = { loja ->
                    lojas.removeAll { it.id == loja.id }
                }
            )

            Destination.Store -> StoreScreen(
                modifier = contentModifier,
                itens = itens,
                nomeLoja = nomeLoja
            )

            Destination.StoreRegistration -> StoreRegistrationScreen(
                modifier = contentModifier,
                lojaInicial = lojaEditando,
                onCancelar = {
                    current = Destination.StoreList
                },

                onSalvar = { loja ->
                    val indice = lojas.indexOfFirst { it.id == loja.id }

                    if (indice >= 0) {
                        //Edição: substitui a loja existente
                        lojas[indice] = loja
                    } else {
                        //Gera o ID da nova loja
                        val novaLoja = loja.copy(
                            id = (lojas.maxOfOrNull { it.id } ?: 0) + 1
                        )

                        lojas.add(novaLoja)
                    }

                    current = Destination.StoreList
                }
            )
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
