package com.daedrii.tictactoe.model

import java.util.Date

data class GameState(
    val id: Long,
    val p1: String,
    val p1Score: String,
    val p2: String,
    val p2Score: String,
    val drawScore: String,
    val currentTurn: Turn,
    val createdAt: Date
)
