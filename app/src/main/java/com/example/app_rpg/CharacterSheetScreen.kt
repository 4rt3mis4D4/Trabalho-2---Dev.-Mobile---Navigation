package com.example.app_rpg

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.LocalModePalette
import com.example.app_rpg.ui.theme.corBrancoOffWhite
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corCinzaEscuro
import com.example.app_rpg.ui.theme.corCinzaMedio
import com.example.app_rpg.ui.theme.corPretoPuro


data class AtributoData(val valor: String, val label: String)
data class StatusData(val valor: String, val label: String)
data class PericiasData(val proficiencia: Int, val bonus: Int, val nome: String)

val atributosMock = listOf(
    AtributoData("1", "For"),
    AtributoData("1", "Des"),
    AtributoData("1", "Con"),
    AtributoData("1", "Int"),
    AtributoData("1", "Sab"),
    AtributoData("1", "Car")
)

val statusMock = listOf(
    StatusData("56", "HP"),
    StatusData("23", "Armad"),
    StatusData("17", "Iniciat"),
    StatusData("9m", "Desloc")
)

val periciaMock = listOf(
    PericiasData(proficiencia = 2, bonus = 2, nome = "Perícia X"),
    PericiasData(proficiencia = 0, bonus = 0, nome = "Perícia Y"),
    PericiasData(proficiencia = 1, bonus = 4, nome = "Perícia Z"),
    PericiasData(proficiencia = 2, bonus = 2, nome = "Perícia A"),
    PericiasData(proficiencia = 0, bonus = 0, nome = "Perícia B"),
    PericiasData(proficiencia = 1, bonus = 4, nome = "Perícia C"),
    PericiasData(proficiencia = 2, bonus = 2, nome = "Perícia U"),
    PericiasData(proficiencia = 0, bonus = 0, nome = "Perícia I"),
    PericiasData(proficiencia = 1, bonus = 4, nome = "Perícia V")
)


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun FichaPersonagemScreenPreview(){
    FichaPersonagemScreen(Modifier)
}

@Composable
fun FichaPersonagemScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(corPretoPuro)
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderSection()
        ClasseRacaSection()
        AtributosSection()
        StatusSection()
        PericiaSection()
        BotoesAcaoSection()
    }
}


@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CampoTextoEstatico(
            texto = "Nome",
            modifier = Modifier.weight(2f)
        )
        CampoTextoEstatico(
            texto = "Level",
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun ClasseRacaSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CampoTextoEstatico(
            texto = "Classe",
            modifier = Modifier.weight(1f)
        )
        CampoTextoEstatico(
            texto = "Raça",
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun AtributosSection() {
    val linhas = atributosMock.chunked(3)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        linhas.forEach { linha ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                linha.forEach { atributo ->
                    CirculoAtributo(valor = atributo.valor, label = atributo.label)
                }
            }
        }
    }
}


@Composable
fun StatusSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        statusMock.forEach { status ->
            CardStatus(
                valor = status.valor,
                label = status.label,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun PericiaSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = corCinzaEscuro),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(2) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    periciaMock.forEach { pericia ->
                        LinhaPericia(
                            proficiencia = pericia.proficiencia,
                            bonus = pericia.bonus,
                            nome = pericia.nome
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BotoesAcaoSection() {
    val palette = LocalModePalette.current
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = corCinzaEscuro,
                contentColor = corBrancoOffWhite
            )
        ) {
            Text(
                text = "Cancelar",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Button(
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.accent,
                contentColor = corBrancoPuro
            )
        ) {
            Text(
                text = "Salvar",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun CampoTextoEstatico(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(corCinzaEscuro)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = texto,
            color = corBrancoOffWhite,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun CirculoAtributo(valor: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(corCinzaEscuro),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = valor,
                    color = corBrancoPuro,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                Text(
                    text = label,
                    color = corCinzaMedio,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 13.sp
                )
            }
        }
    }
}


@Composable
fun CardStatus(valor: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = corCinzaEscuro),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = valor,
                color = corBrancoPuro,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 22.sp
            )
            Text(
                text = label,
                color = corCinzaMedio,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 13.sp
            )
        }
    }
}


@Composable
fun LinhaPericia(proficiencia: Int, bonus: Int, nome: String) {
    val palette = LocalModePalette.current
    val corIndicador = when {
        proficiencia >= 2 -> palette.accent
        proficiencia == 1 -> corCinzaMedio
        else -> corCinzaEscuro.copy(alpha = 0.5f)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(corIndicador)
                .border(
                    width = 1.5.dp,
                    color = corCinzaMedio.copy(alpha = 0.4f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(corPretoPuro.copy(alpha = 0.35f))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = bonus.toString(),
                color = corBrancoOffWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = nome,
            color = corBrancoOffWhite,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}
