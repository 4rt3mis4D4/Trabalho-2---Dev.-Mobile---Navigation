package com.example.app_rpg.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ModePalette(
    val accent: Color,
    val strong: Color,
    val soft: Color,
    val surface: Color
)

val PlayerPalette = ModePalette(
    accent = corJogadorPrincipal,
    strong = corJogadorPrincipal,
    soft = corJogadorDestaque,
    surface = corJogadorEscuro
)

val MasterPalette = ModePalette(
    accent = corMestreDestaque,
    strong = corMestrePrincipal,
    soft = corMestreDestaque,
    surface = corMestreSombra
)

val LocalModePalette = staticCompositionLocalOf { PlayerPalette }
