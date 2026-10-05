package com.example.app_rpg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corJogadorDestaque
import com.example.app_rpg.ui.theme.corJogadorEscuro
import com.example.app_rpg.ui.theme.corJogadorPrincipal
import com.example.app_rpg.ui.theme.corMestreDestaque
import com.example.app_rpg.ui.theme.corMestrePrincipal
import com.example.app_rpg.ui.theme.corMestreSombra

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    mode: Mode,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null
) {
    val (backgroundColor, accentColor, subtitleColor) = when (mode) {
        Mode.Master -> Triple(corMestreSombra, corMestrePrincipal, corMestreDestaque)
        Mode.Player -> Triple(corJogadorEscuro, corJogadorPrincipal, corJogadorDestaque)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        CenterAlignedTopAppBar(
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            color = subtitleColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            navigationIcon = {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.icon_arrow_back),
                            contentDescription = "Voltar"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = corBrancoPuro,
                navigationIconContentColor = corBrancoPuro
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(accentColor)
        )
    }
}

@Preview
@Composable
private fun AppTopBarPreview() {
    AppTopBar(
        title = "Ferreiro",
        mode = Mode.Master,
        subtitle = "3 itens à venda",
        onBack = {}
    )
}
