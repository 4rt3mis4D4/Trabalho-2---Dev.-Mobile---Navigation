package com.example.app_rpg

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.LocalModePalette
import com.example.app_rpg.ui.theme.corBrancoOffWhite
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corCinzaEscuro
import com.example.app_rpg.ui.theme.corCinzaMedio
import com.example.app_rpg.ui.theme.corPretoPuro

data class LojaRpg(
    val id: Int,
    val nome : String,
    val descricao: String,
    val imagemUri: Uri?,
    val visivel: Boolean,
    val itemIds: Set<Int> = emptySet()
)

@Composable
fun StoreRegistrationScreen(
    modifier: Modifier = Modifier,
    lojaInicial: LojaRpg? = null,
    items: List<ItemRpg> = emptyList(),
    onCancelar: () -> Unit = {},
    onSalvar: (LojaRpg) -> Unit = {}
) {
    val palette = LocalModePalette.current
    val context = LocalContext.current

    var nome by rememberSaveable {
        mutableStateOf(lojaInicial?.nome ?: "")
    }

    var descricao by rememberSaveable {
        mutableStateOf(lojaInicial?.descricao ?: "")
    }

    var visivel by rememberSaveable {
        mutableStateOf(lojaInicial?.visivel ?: true)
    }

    var itemIds by rememberSaveable {
        mutableStateOf(lojaInicial?.itemIds.orEmpty())
    }

    var imagemUri by remember {
        mutableStateOf(lojaInicial?.imagemUri)
    }

    var errorName by rememberSaveable {
        mutableStateOf(false)
    }

    val selecionarImagem = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imagemUri = uri
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(corPretoPuro)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // NOME
        FieldLabel("Nome")

        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it

                if (it.isNotBlank()) {
                    errorName = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Nome da loja",
                    color = corCinzaMedio
                )
            },
            isError = errorName,
            supportingText = {
                if (errorName) {
                    Text(
                        text = "Informe o nome da loja.",
                        color = palette.soft
                    )
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = corCinzaEscuro,
                unfocusedContainerColor = corCinzaEscuro,
                focusedTextColor = corBrancoPuro,
                unfocusedTextColor = corBrancoOffWhite,
                focusedIndicatorColor = palette.accent,
                unfocusedIndicatorColor = corCinzaMedio,
                cursorColor = palette.accent
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // IMAGEM
        FieldLabel("Imagem")

        OutlinedButton(
            onClick = {
                selecionarImagem.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = corCinzaEscuro,
                contentColor = corBrancoPuro
            )
        ) {
            Text(
                text = if (imagemUri == null) {
                    "Selecionar imagem"
                } else {
                    "Imagem selecionada"
                }
            )
        }

        if (imagemUri != null) {
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Imagem carregada",
                color = palette.soft,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // DESCRIÇÃO
        FieldLabel("Descrição")

        OutlinedTextField(
            value = descricao,
            onValueChange = {
                descricao = it
            },
            placeholder = {
                Text(
                    text = "Digite uma descrição para a loja...",
                    color = corCinzaMedio
                )
            },
            minLines = 6,
            maxLines = 10,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = corCinzaEscuro,
                unfocusedContainerColor = corCinzaEscuro,
                focusedTextColor = corBrancoOffWhite,
                unfocusedTextColor = corBrancoOffWhite,
                focusedIndicatorColor = palette.strong,
                unfocusedIndicatorColor = corCinzaMedio,
                cursorColor = palette.accent
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // VISIBILIDADE
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = visivel,
                onCheckedChange = {
                    visivel = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = palette.strong,
                    uncheckedColor = corCinzaMedio,
                    checkmarkColor = corBrancoPuro
                )
            )

            Text(
                text = "Visível para os jogadores",
                color = corBrancoOffWhite,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        FieldLabel("Itens à venda (${itemIds.size})")

        StoreItemsSelector(
            items = items,
            selectedIds = itemIds,
            onToggle = { id ->
                itemIds = if (id in itemIds) itemIds - id else itemIds + id
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // BOTÕES
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            //Cancelar
            OutlinedButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Cadastro cancelado",
                        Toast.LENGTH_SHORT
                    ).show()

                    onCancelar()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = palette.accent
                )
            ) {
                Text(
                    text = "CANCELAR",
                    fontWeight = FontWeight.Bold
                )
            }

            //Salvar
            Button(
                onClick = {
                    if (nome.isBlank()) {
                        errorName = true
                    } else {
                        val loja = LojaRpg(
                            id = lojaInicial?.id ?: 0,
                            nome = nome.trim(),
                            descricao = descricao.trim(),
                            imagemUri = imagemUri,
                            visivel = visivel,
                            itemIds = itemIds
                        )

                        Toast.makeText(
                            context,
                            "Loja ${loja.nome} salva!",
                            Toast.LENGTH_SHORT
                        ).show()

                        onSalvar(loja)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.accent,
                    contentColor = corBrancoPuro
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "SALVAR",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = corCinzaMedio,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun StoreItemsSelector(
    items: List<ItemRpg>,
    selectedIds: Set<Int>,
    onToggle: (Int) -> Unit
) {
    val palette = LocalModePalette.current

    if (items.isEmpty()) {
        Text(
            text = "Nenhum item cadastrado.\nCrie itens na aba Itens para vendê-los aqui.",
            color = corCinzaMedio,
            fontSize = 13.sp
        )
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { item ->
            val selected = item.id in selectedIds

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) palette.surface else corCinzaEscuro)
                    .border(
                        width = 1.dp,
                        color = if (selected) palette.strong else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .toggleable(
                        value = selected,
                        role = Role.Checkbox,
                        onValueChange = { onToggle(item.id) }
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = palette.strong,
                        uncheckedColor = corCinzaMedio,
                        checkmarkColor = corBrancoPuro
                    )
                )

                Icon(
                    painter = painterResource(item.itemCat.iconsRes),
                    contentDescription = item.itemCat.nome,
                    tint = if (selected) corBrancoPuro else corCinzaMedio,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = item.itemName,
                    color = if (selected) corBrancoPuro else corBrancoOffWhite,
                    fontSize = 15.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(R.drawable.icon_money),
                    contentDescription = null,
                    tint = palette.soft,
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = item.itemValue.toString(),
                    color = palette.soft,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
