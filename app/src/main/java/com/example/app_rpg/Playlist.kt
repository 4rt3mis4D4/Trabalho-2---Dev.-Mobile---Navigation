package com.example.app_rpg

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corCinzaEscuro
import com.example.app_rpg.ui.theme.corCinzaMedio
import com.example.app_rpg.ui.theme.corMestreDestaque
import com.example.app_rpg.ui.theme.corPretoPuro

private val generos = listOf("Pop", "Rock", "MPB")

@Composable
fun Playlist(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(corPretoPuro)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = corCinzaEscuro,
                    unfocusedContainerColor = corCinzaEscuro,
                    focusedTextColor = corBrancoPuro,
                    unfocusedTextColor = corBrancoPuro,
                    focusedPlaceholderColor = corCinzaMedio,
                    unfocusedPlaceholderColor = corCinzaMedio
                ),
                placeholder = { Text("Pesquisar") },
                shape = RoundedCornerShape(10.dp)
            )

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = corMestreDestaque)
            ) {
                Text("🔍")
            }
        }

        // Filtros de gêneros musicais
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            generos.forEach { genero ->
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = corMestreDestaque)
                ) {
                    Text(genero)
                }
            }
        }

        //Espaço entre gêneros e lista de musicas
        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(3) {
                MusicaCard()
            }
        }
    }
}

@Composable
private fun MusicaCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(containerColor = corCinzaEscuro)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            //Espaço da imagem
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(corMestreDestaque, shape = RoundedCornerShape(50.dp))
            )
            Text(
                text = "Nome da música",
                fontSize = 18.sp,
                color = corBrancoPuro,
                modifier = Modifier.padding(start = 30.dp)
            )
        }
    }
}
