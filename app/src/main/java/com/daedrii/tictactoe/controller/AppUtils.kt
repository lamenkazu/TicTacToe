package com.daedrii.tictactoe.controller

import android.annotation.SuppressLint
import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random

class AppUtils(context: Context) {

    companion object {
        const val DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss"
    }
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)

    private var adapter = GameStateAdapter(context)

    // Gera um ID aleatório usando um objeto Random
    fun generateRandomId(): Long {
        adapter.updateGameStateList()
        return Random().nextLong()
    }

    // Obtém a data e hora formatada como String a partir de um Date
    fun getCurrentTimestamp(date: Date): String {
        return dateFormat.format(date)
    }

    // Obtém a data a partir de uma string formatada no padrão do código
    fun parseCreatedAtDate(createdAt: String?): Date? {
        if (createdAt.isNullOrBlank()) return null

        return try {
            dateFormat.parse(createdAt)
        } catch (e: ParseException) {
            // Em caso de erro na análise, imprime o erro e retorna nulo
            e.printStackTrace()
            null
        }
    }
}