package com.example.app_rpg

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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

private const val MESSAGE_SUCESS = "Item adicionado com sucesso"

@Composable
fun ItemRegistrationScreen(
    modifier: Modifier = Modifier,
    itens: SnapshotStateList<ItemRpg>,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var nome by rememberSaveable { mutableStateOf("") }
    var valor by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var categoriaSelecionada by rememberSaveable { mutableIntStateOf(0) }
    var salvar by rememberSaveable { mutableStateOf(false) }

    val nomeInvalido = salvar && nome.isBlank()
    val valorInvalido = salvar && valor.isBlank()

    fun adicionarItem() {
        salvar = true
        val valorNumerico = valor.toIntOrNull()
        if (nome.isBlank() || valorNumerico == null) return

        itens.add(
            ItemRpg(
                id = (itens.maxOfOrNull { it.id } ?: 0) + 1,
                itemName = nome.trim(),
                itemValue = valorNumerico,
                itemDescription = descricao.trim(),
                itemCat = listCatItem[categoriaSelecionada]
            )
        )

        nome = ""
        valor = ""
        descricao = ""
        salvar = false
        focusManager.clearFocus()

        Toast.makeText(context, MESSAGE_SUCESS, Toast.LENGTH_SHORT).show()

        // TODO: Implementar navegação para a tela de destino
        // navController.navigate(Rotas.TELA_DESTINO)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(corPretoPuro)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Categoria",
                color = corBrancoOffWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            SelectCategory(
                selecionada = categoriaSelecionada,
                onSelecionar = { categoriaSelecionada = it }
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = corMestreSombra),
                border = BorderStroke(1.dp, corMestrePrincipal)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RegistrationField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = "Nome do item",
                        iconeRes = listCatItem[categoriaSelecionada].iconsRes,
                        isError = nomeInvalido,
                        mensagemErro = "Informe o nome do item",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    RegistrationField(
                        value = valor,
                        onValueChange = { novo -> valor = novo.filter { it.isDigit() }.take(9) },
                        label = "Valor do item",
                        iconeRes = R.drawable.icon_money,
                        isError = valorInvalido,
                        mensagemErro = "Informe o valor do item",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    RegistrationField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        label = "Descrição do item",
                        iconeRes = R.drawable.icon_edit,
                        shape = RoundedCornerShape(24.dp),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }

            Button(
                onClick = { adicionarItem() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = corMestreDestaque,
                    contentColor = corBrancoPuro
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_add),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Adicionar item",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SelectCategory(
    selecionada: Int,
    onSelecionar: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listCatItem.forEachIndexed { index, categoria ->
            val ativa = index == selecionada

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .selectable(
                        selected = ativa,
                        role = Role.RadioButton,
                        onClick = { onSelecionar(index) }
                    )
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (ativa) corMestrePrincipal else corCinzaEscuro)
                        .border(
                            width = 2.dp,
                            color = if (ativa) corMestreDestaque else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(categoria.iconsRes),
                        contentDescription = categoria.nome,
                        tint = if (ativa) corBrancoPuro else corCinzaMedio,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = categoria.nome,
                    color = if (ativa) corBrancoPuro else corCinzaMedio,
                    fontSize = 10.sp,
                    fontWeight = if (ativa) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun RegistrationField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    @DrawableRes iconeRes: Int,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    isError: Boolean = false,
    mensagemErro: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = {
            Icon(
                painter = painterResource(iconeRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        isError = isError,
        supportingText = if (isError) {
            { Text(mensagemErro) }
        } else null,
        singleLine = maxLines == 1,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = corBrancoPuro,
            unfocusedTextColor = corBrancoPuro,
            focusedContainerColor = corCinzaEscuro,
            unfocusedContainerColor = corCinzaEscuro,
            focusedBorderColor = corMestreDestaque,
            unfocusedBorderColor = corMestrePrincipal,
            focusedLabelColor = corMestreDestaque,
            unfocusedLabelColor = corCinzaMedio,
            focusedLeadingIconColor = corMestreDestaque,
            unfocusedLeadingIconColor = corCinzaMedio,
            cursorColor = corMestreDestaque
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CadastroItensScreenPreview() {
    App_rpgTheme {
        ItemRegistrationScreen(itens = remember { mutableStateListOf<ItemRpg>() })
    }
}
