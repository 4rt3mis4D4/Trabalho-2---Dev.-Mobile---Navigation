package com.example.app_rpg

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.corBrancoOffWhite
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corCinzaMedio
import com.example.app_rpg.ui.theme.corJogadorDestaque
import com.example.app_rpg.ui.theme.corJogadorEscuro
import com.example.app_rpg.ui.theme.corJogadorPrincipal
import com.example.app_rpg.ui.theme.corPretoPuro

private const val MESSAGE_BUY = "Compra efetuada com sucesso!"

@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    itens: List<ItemRpg>,
    onItemClick: (ItemRpg) -> Unit = {
        // TODO: Implementar navegação tela lista de lojas para lista de itens salvos na loja
    }
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(corPretoPuro)
    ) {
        if (itens.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum item na loja ainda.\nCadastre um item para ele aparecer aqui.",
                    color = corCinzaMedio,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(itens, key = { it.id }) { item ->
                    ItemCard(
                        item = item,
                        onClick = { onItemClick(item) },
                        onComprar = {
                            Toast.makeText(context, MESSAGE_BUY, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemCard(
    item: ItemRpg,
    onClick: () -> Unit,
    onComprar: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = corJogadorEscuro),
        border = BorderStroke(1.dp, corJogadorPrincipal)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(corJogadorPrincipal),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(item.itemCat.iconsRes),
                    contentDescription = item.itemCat.nome,
                    tint = corBrancoPuro,
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(corJogadorPrincipal)
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = item.itemName,
                        color = corBrancoPuro,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = item.itemDescription.ifBlank { "Sem descrição" },
                    color = corBrancoOffWhite,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(corJogadorDestaque)
                        .padding(horizontal = 10.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_money),
                        contentDescription = null,
                        tint = corJogadorEscuro,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = item.itemValue.toString(),
                        color = corJogadorEscuro,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            FilledIconButton(
                onClick = onComprar,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = corJogadorPrincipal,
                    contentColor = corBrancoPuro
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_coins),
                    contentDescription = "Comprar ${item.itemName}",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}