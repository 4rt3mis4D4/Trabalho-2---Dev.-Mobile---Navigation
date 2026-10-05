package com.example.app_rpg

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.App_rpgTheme
import com.example.app_rpg.ui.theme.corBrancoOffWhite
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corCinzaEscuro
import com.example.app_rpg.ui.theme.corCinzaMedio
import com.example.app_rpg.ui.theme.corMestreDestaque
import com.example.app_rpg.ui.theme.corMestrePrincipal
import com.example.app_rpg.ui.theme.corMestreSombra
import com.example.app_rpg.ui.theme.corPretoPuro

@Composable
fun StoreListScreen(
    modifier: Modifier = Modifier,
    lojas: List<LojaRpg>,
    //Master manages the stores; players only see the visible ones
    isMaster: Boolean = true,
    onLojaClick: (LojaRpg) -> Unit = {},
    onAdicionar: () -> Unit = {},
    onEditar: (LojaRpg) -> Unit = {},
    onExcluir: (LojaRpg) -> Unit = {},
    onToggleVisibility: (LojaRpg) -> Unit = {}
) {
    val context = LocalContext.current

    val displayedStores = if (isMaster) lojas else lojas.filter { it.visivel }

    //Loja aguardando confirmação de exclusão
    var lojaParaExcluir by remember {
        mutableStateOf<LojaRpg?>(null)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(corPretoPuro)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CabecalhoLojas()

            if (displayedStores.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isMaster) {
                            "Nenhuma loja cadastrada ainda.\nToque em + para criar uma loja."
                        } else {
                            "Nenhuma loja disponível no momento."
                        },
                        color = corCinzaMedio,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    //Espaço extra embaixo para o botão + não cobrir o último card
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 20.dp,
                        end = 20.dp,
                        bottom = 96.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(displayedStores, key = { it.id }) { loja ->
                        LojaCard(
                            loja = loja,
                            isMaster = isMaster,
                            onClick = { onLojaClick(loja) },
                            onEditar = { onEditar(loja) },
                            onExcluir = { lojaParaExcluir = loja },
                            onToggleVisibility = { onToggleVisibility(loja) }
                        )
                    }
                }
            }
        }

        if (isMaster) {
            FloatingActionButton(
                onClick = onAdicionar,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                shape = CircleShape,
                containerColor = corMestreDestaque,
                contentColor = corBrancoPuro
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_add),
                    contentDescription = "Criar loja"
                )
            }
        }
    }

    lojaParaExcluir?.let { loja ->
        AlertDialog(
            onDismissRequest = { lojaParaExcluir = null },
            containerColor = corCinzaEscuro,
            titleContentColor = corBrancoPuro,
            textContentColor = corBrancoOffWhite,
            title = { Text("Excluir loja") },
            text = { Text("Tem certeza que deseja excluir a loja \"${loja.nome}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onExcluir(loja)
                        Toast.makeText(
                            context,
                            "Loja ${loja.nome} excluída",
                            Toast.LENGTH_SHORT
                        ).show()
                        lojaParaExcluir = null
                    }
                ) {
                    Text("EXCLUIR", color = corMestreDestaque, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { lojaParaExcluir = null }) {
                    Text("CANCELAR", color = corCinzaMedio, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun CabecalhoLojas() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(corMestreSombra)
            .statusBarsPadding()
    ) {
        Text(
            text = "Lojas",
            color = corBrancoPuro,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(corMestrePrincipal)
        )
    }
}

@Composable
private fun LojaCard(
    loja: LojaRpg,
    isMaster: Boolean,
    onClick: () -> Unit,
    onEditar: () -> Unit,
    onExcluir: () -> Unit,
    onToggleVisibility: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = corMestreSombra),
        border = BorderStroke(1.dp, if (loja.visivel) corMestrePrincipal else corCinzaMedio)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AvatarLoja(
                visivel = loja.visivel,
                //Only the master can hide/show the store by tapping the image
                onClick = if (isMaster) onToggleVisibility else null
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(corMestrePrincipal)
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = loja.nome,
                        color = corBrancoPuro,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = loja.descricao.ifBlank { "Sem descrição" },
                    color = corBrancoOffWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isMaster) Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledIconButton(
                    onClick = onEditar,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = corMestrePrincipal,
                        contentColor = corBrancoPuro
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_edit),
                        contentDescription = "Editar ${loja.nome}",
                        modifier = Modifier.size(20.dp)
                    )
                }

                FilledIconButton(
                    onClick = onExcluir,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = corCinzaEscuro,
                        contentColor = corMestreDestaque
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_delete),
                        contentDescription = "Excluir ${loja.nome}",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AvatarLoja(visivel: Boolean, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(if (visivel) corMestrePrincipal else corCinzaEscuro)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        onClickLabel = if (visivel) "Ocultar dos jogadores" else "Mostrar aos jogadores",
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_store),
            contentDescription = if (visivel) "Loja visível" else null,
            tint = if (visivel) corBrancoPuro else corCinzaMedio,
            modifier = Modifier.size(40.dp)
        )

        //Loja oculta: olho cortado por cima do ícone da loja
        if (!visivel) {
            Icon(
                painter = painterResource(R.drawable.icon_visibility_off),
                contentDescription = "Loja oculta para os jogadores",
                tint = corBrancoPuro,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StoreListScreenPreview() {
    val lojas = remember {
        mutableStateListOf(
            LojaRpg(1, "Ferreiro Anão", "Armas e armaduras forjadas nas montanhas.", null, false),
            LojaRpg(2, "Taverna do Javali", "Comida quente e bebida gelada.", null, true),
            LojaRpg(3, "Mercado Negro", "Itens raros, sem perguntas.", null, false),
            LojaRpg(4, "Estábulo Real", "Montarias para longas viagens.", null, true),
            LojaRpg(5, "Empório Geral", "Ferramentas e suprimentos de aventura.", null, true)
        )
    }

    App_rpgTheme {
        StoreListScreen(
            lojas = lojas,
            onExcluir = { lojas.remove(it) }
        )
    }
}
