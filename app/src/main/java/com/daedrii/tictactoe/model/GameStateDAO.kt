package com.daedrii.tictactoe.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.daedrii.tictactoe.controller.AppUtils
import java.text.SimpleDateFormat

class GameStateDAO(context: Context):SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{

        private const val DB_VERSION = 1
        private const val DB_NAME = "game_state_db"
        private const val TABLE_GAMESTATE = "game_state"

        private const val KEY_ID = "id"
        private const val KEY_P1_NAME = "p1"
        private const val KEY_P1_SCORE = "p1_score"
        private const val KEY_P2_NAME = "p2"
        private const val KEY_P2_SCORE = "p2_score"
        private const val KEY_DRAW_SCORE = "draw_score"
        private const val KEY_CURRENT_TURN = "current_turn"
        private const val KEY_CREATED_AT = "created_at"
    }

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(AppUtils.DATE_FORMAT_PATTERN) // Especifique o formato desejado

    // Criação do banco de dados - chama apenas uma vez
    override fun onCreate(db: SQLiteDatabase?) {
        val sqlQuerry = "CREATE TABLE $TABLE_GAMESTATE (" +
                "$KEY_ID BIGINT PRIMARY KEY, " +
                "$KEY_P1_NAME TEXT, " +
                "$KEY_P1_SCORE TEXT, " +
                "$KEY_P2_NAME TEXT, " +
                "$KEY_P2_SCORE TEXT, " +
                "$KEY_DRAW_SCORE TEXT, " +
                "$KEY_CURRENT_TURN TEXT, " +
                "$KEY_CREATED_AT TEXT " +
                ")"
        db?.execSQL(sqlQuerry)
    }

    // Método chamado quando há uma atualização do banco de dados
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_GAMESTATE")
        onCreate(db)
    }

    // Insere um novo estado de jogo no banco de dados
    fun insert(gameState: GameState){
        val db = writableDatabase
        val values = ContentValues()

        values.put(KEY_ID, gameState.id)
        values.put(KEY_P1_NAME, gameState.p1)
        values.put(KEY_P1_SCORE, gameState.p1Score)
        values.put(KEY_P2_NAME, gameState.p2)
        values.put(KEY_P2_SCORE, gameState.p2Score)
        values.put(KEY_DRAW_SCORE, gameState.drawScore)
        values.put(KEY_CURRENT_TURN, gameState.currentTurn.name)

        val createdAt = dateFormat.format(gameState.createdAt)

        values.put(KEY_CREATED_AT, createdAt)

        db.insert(TABLE_GAMESTATE, null, values)

    }

    // Retorna uma lista de todos os jogos salvos
    // no banco ordenado pela data de criação de forma decrescente
    @SuppressLint("Range")
    fun getAll(): ArrayList<GameState>{

        val db = readableDatabase
        val selectQuerry = "SELECT * FROM $TABLE_GAMESTATE ORDER BY $KEY_CREATED_AT DESC"
        val gameStateList = ArrayList<GameState>()

        val cursor: Cursor? = db.rawQuery(selectQuerry, null)

        cursor?.apply {
            if(moveToFirst()){
                do{

                    val id = getLong(getColumnIndex(KEY_ID))
                    val p1 = getString(getColumnIndex(KEY_P1_NAME))
                    val p1Score = getString(getColumnIndex(KEY_P1_SCORE))
                    val p2 = getString(getColumnIndex(KEY_P2_NAME))
                    val p2Score = getString(getColumnIndex(KEY_P2_SCORE))
                    val drawScore = getString(getColumnIndex(KEY_DRAW_SCORE))
                    val currentTurn = getString(getColumnIndex(KEY_CURRENT_TURN))
                    val createdAt = getString(getColumnIndex(KEY_CREATED_AT))

                    val createdAtDate = dateFormat.parse(createdAt)

                    // Converte a string para o enum Turn
                    val turnEnum = when (currentTurn) {
                        "NOUGHT" -> Turn.NOUGHT
                        "CROSS" -> Turn.CROSS
                        else -> Turn.NOUGHT // Ou um valor padrão se a string não for reconhecida
                    }

                    // Cria uma instância do GameState e adiciona à lista
                    createdAtDate?.let {
                        GameState(id, p1, p1Score, p2, p2Score, drawScore, turnEnum,it)
                    }?.let { gameStateList.add(it) }

                }while(moveToNext())
            }
            close()
        }

        return gameStateList
    }

    // Exclui um jogo do historico com base no ID
    fun deleteById(matchId: Long){
        val db = writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf(matchId.toString())

        db?.delete(TABLE_GAMESTATE, whereClause, whereArgs)
    }

    // Verifica se um jogo com o ID fornecido existe no banco
    @SuppressLint("Recycle")
    fun checkIfMatchExists(matchId: Long): Boolean{
        val db = writableDatabase
        val cursor = db?.query(
            TABLE_GAMESTATE,
                                null,
                                "id = ?",
                                arrayOf(matchId.toString()),
                                null,
                                null,
                                null
        )

        return (cursor?.count ?: 0) > 0
    }


}



