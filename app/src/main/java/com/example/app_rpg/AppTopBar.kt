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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.LocalModePalette
import com.example.app_rpg.ui.theme.MasterPalette
import com.example.app_rpg.ui.theme.corBrancoPuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null
) {
    val palette = LocalModePalette.current

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
                            color = palette.soft,
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
                containerColor = palette.surface,
                titleContentColor = corBrancoPuro,
                navigationIconContentColor = corBrancoPuro
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(palette.strong)
        )
    }
}

@Preview
@Composable
private fun AppTopBarPreview() {
    CompositionLocalProvider(LocalModePalette provides MasterPalette) {
        AppTopBar(
            title = "Ferreiro",
            subtitle = "3 itens à venda",
            onBack = {}
        )
    }
}
