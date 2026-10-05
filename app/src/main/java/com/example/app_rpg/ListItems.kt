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
    itemCategory("Armas", R.drawable.icon_armament),
    itemCategory("Alimentos", R.drawable.icon_food),
    itemCategory("Armaduras", R.drawable.icon_armor),
    itemCategory("Ferramentas", R.drawable.icon_tools),
    itemCategory("Montarias", R.drawable.icon_mount)
)