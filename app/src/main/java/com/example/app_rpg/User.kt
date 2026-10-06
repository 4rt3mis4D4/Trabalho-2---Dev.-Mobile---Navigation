package com.example.app_rpg

import java.io.Serializable

data class User(
    val mode: Mode = Mode.Player
) : Serializable
