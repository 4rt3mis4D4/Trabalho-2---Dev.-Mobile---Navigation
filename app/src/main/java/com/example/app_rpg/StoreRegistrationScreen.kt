package com.example.app_rpg

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.corBrancoOffWhite
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corCinzaEscuro
import com.example.app_rpg.ui.theme.corCinzaMedio
import com.example.app_rpg.ui.theme.corMestreDestaque
import com.example.app_rpg.ui.theme.corMestrePrincipal
import com.example.app_rpg.ui.theme.corPretoPuro

data class LojaRpg(
    val id: Int,
    val nome : String,
    val descricao: String,
    val imagemUri: Uri?,
    val visivel: Boolean
)

@Composable
fun StoreRegistrationScreen(
    modifier: Modifier = Modifier,
    onCancelar: () -> Unit = {},
    onSalvar: (LojaRpg) -> Unit = {}
) {

    val context = LocalContext.current

    var nome by rememberSaveable {
        mutableStateOf("")
    }

    var descricao by rememberSaveable {
        mutableStateOf("")
    }

    var visivel by rememberSaveable {
        mutableStateOf(true)
    }

    var imagemUri by remember {
        mutableStateOf<Uri?>(null)
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
        // CABEÇALHO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {

            Text(
                text = "CRIAR LOJA",
                color = corBrancoPuro,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )

            Text(
                text = "MESTRE",
                color = corMestreDestaque,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // NOME
        Text(
            text = "Nome",
            color = corCinzaMedio,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

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
                        color = corMestreDestaque
                    )
                }
            },

            singleLine = true,

            keyboardOptions = KeyboardOptions(),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = corCinzaEscuro,
                unfocusedContainerColor = corCinzaEscuro,

                focusedTextColor = corBrancoPuro,
                unfocusedTextColor = corBrancoOffWhite,

                focusedIndicatorColor = corMestreDestaque,
                unfocusedIndicatorColor = corCinzaMedio,

                cursorColor = corMestreDestaque
            ),

            shape = RoundedCornerShape(10.dp)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // IMAGEM
        Text(
            text = "Imagem",
            color = corCinzaMedio,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

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

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Imagem carregada",
                color = corMestreDestaque,
                fontSize = 12.sp
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // DESCRIÇÃO
        Text(
            text = "Descrição",
            color = corCinzaMedio,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

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

                focusedIndicatorColor = corMestrePrincipal,
                unfocusedIndicatorColor = corCinzaMedio,

                cursorColor = corMestreDestaque
            ),

            shape = RoundedCornerShape(10.dp)
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

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
                    checkedColor = corMestrePrincipal,
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

        Spacer(
            modifier = Modifier.height(32.dp)
        )

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
                    contentColor = corMestreDestaque
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
                            id = 0,
                            nome = nome.trim(),
                            descricao = descricao.trim(),
                            imagemUri = imagemUri,
                            visivel = visivel
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
                    containerColor = corMestreDestaque,
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