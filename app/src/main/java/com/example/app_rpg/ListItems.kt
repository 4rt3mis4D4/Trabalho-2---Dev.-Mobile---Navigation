package com.example.app_rpg

import androidx.annotation.DrawableRes

data class itemCategory(
    val nome: String,
    @param:DrawableRes val iconsRes: Int
)

data class ItemRpg(
    val id: Int,
    val itemName: String,
    val itemValue: Int,
    val itemDescription: String,
    val itemCat: itemCategory
)

val listCatItem = listOf(
    itemCategory("Armas", R.drawable.swords_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
    itemCategory("Alimentos", R.drawable.restaurant_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
    itemCategory("Armaduras", R.drawable.shield_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
    itemCategory("Ferramentas", R.drawable.business_center_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
    itemCategory("Montarias", R.drawable.chess_knight_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
)