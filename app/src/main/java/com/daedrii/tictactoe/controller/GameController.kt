package com.daedrii.tictactoe.controller

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.daedrii.tictactoe.R
import com.daedrii.tictactoe.databinding.ActivityMainBinding
import com.daedrii.tictactoe.databinding.FragmentBoardBinding
import com.daedrii.tictactoe.model.GameState
import com.daedrii.tictactoe.model.Turn
import java.util.Date

class GameController(private val context: Context, private val bindingMain: ActivityMainBinding, private val bindingBoard: FragmentBoardBinding) {

    // Variáveis para controlar a pontuação e estado do jogo
    private var crossScore = 0
    private var noughtScore = 0
    private var drawScore = 0
    private var matchId = -1L
    private var firstTurn = Turn.NOUGHT
    private var currentTurn = Turn.NOUGHT
    private var boardList = mutableListOf<Button>()
    private var gameStopped: Boolean = false
    private var isRobot: Boolean = false
    private var resources = context.resources
    private lateinit var createdAt: Date

    private val utils = GameUtils(context, bindingMain)// Utilitário para a lógica do jogo

    // Armazena os backgrounds originais dos labels dos jogadores
    private val originalBackgroundP1: Drawable = bindingMain.lblP1.background
    private val originalBackgroundP2: Drawable = bindingMain.lblP2.background

    // Inicializa o jogo com os dados recebidos da match
    fun initGame(match: GameState) {

        // Define os nomes dos jogadores
        bindingMain.lblP1.text = match.p1
        bindingMain.lblP2.text = match.p2

        //E a pontuação
        bindingMain.p1Score.text = match.p1Score
        bindingMain.p2Score.text = match.p2Score
        bindingMain.drawScore.text = match.drawScore
        crossScore = match.p2Score.toInt()
        noughtScore = match.p1Score.toInt()
        drawScore = match.drawScore.toInt()

        // Define o ID da partida e o turno atual
        matchId = match.id
        currentTurn = match.currentTurn
        createdAt = match.createdAt

        // Verifica se o oponente é um robô
        if(match.p2 == resources.getString(R.string.robot_name)){
            isRobot = true
        }

        // Define o background do jogador atual
        if(currentTurn == Turn.NOUGHT){
            bindingMain.lblP2.background = null
        }else{
            bindingMain.lblP1.background = null
        }

        // Inicializa o tabuleiro do jogo
        initBoard()
    }

    // Sai do jogo e salva o estado atual
    fun leaveGame(){
        saveGameState()
        (context as Activity).finish()
    }

    // Inicializa o tabuleiro do jogo e define os botões do tabuleiro
    private fun initBoard(){
        utils.changeTurnLabel(currentTurn)

        // Adiciona botões à lista do tabuleiro
        boardList.add(bindingBoard.a1)
        boardList.add(bindingBoard.a2)
        boardList.add(bindingBoard.a3)
        boardList.add(bindingBoard.b1)
        boardList.add(bindingBoard.b2)
        boardList.add(bindingBoard.b3)
        boardList.add(bindingBoard.c1)
        boardList.add(bindingBoard.c2)
        boardList.add(bindingBoard.c3)

        // Associa um listener a cada botão do tabuleiro
        for(button in boardList){
            button.setOnClickListener {
                boardTapped(it)
            }
        }

        // Se o oponente for um robô e for a vez do robô jogar (Turn.CROSS), o robô faz sua jogada
        if(isRobot && currentTurn == Turn.CROSS){
            handleBotMove()
        }

    }

    // Lida com o evento de clique em um botão do tabuleiro
    private fun boardTapped(view: View){

        if(view !is Button) return

        addToBoard(view)

        if(!gameStopped){
            //Checa se bolinha (sempre o jogador1) ganhou
            if(checkForVictory(utils.getNoughtString())){
                val res = resources.getString(R.string.nought_win)
                result(res)
                gameStopped = true
                noughtScore++
                bindingMain.p1Score.text = noughtScore.toString()
                firstTurn = Turn.CROSS
                saveGameState()
            }

            //Checa se X (sempre jogador 2) ganhou.
            if(checkForVictory(utils.getCrossString())){
                val res = resources.getString(R.string.cross_win)
                result(res)
                gameStopped = true
                crossScore++
                bindingMain.p2Score.text = crossScore.toString()
                firstTurn = Turn.NOUGHT
                saveGameState()
            }
        }

        // Checa se o tabuleiro está cheio e não houve vitória
        if(utils.isBoardFull(boardList) && !gameStopped){
            result(resources.getString(R.string.draw))
            gameStopped = true
            drawScore++
            bindingMain.drawScore.text = drawScore.toString()
            saveGameState()
        }

    }

    // Adiciona uma jogada ao tabuleiro
    private fun addToBoard(button: Button) {
        if(!gameStopped){

            if(button.text.isNotBlank()) return

            if(currentTurn == Turn.NOUGHT){

                button.text = utils.getNoughtString()
                currentTurn = Turn.CROSS
                bindingMain.lblP2.background = originalBackgroundP2
                bindingMain.lblP1.background = null

                if(isRobot && !checkForVictory(utils.getNoughtString())){
                    handleBotMove()
                }

            }else{
                button.text = utils.getCrossString()
                currentTurn = Turn.NOUGHT
                bindingMain.lblP2.background = null
                bindingMain.lblP1.background = originalBackgroundP1

            }

            utils.changeTurnLabel(currentTurn)

        }

    }

    // Lida com a jogada do robô
    private fun handleBotMove(){
        for(btn in boardList){
            if(btn.text == ""){
                btn.text = utils.getCrossString()
                currentTurn = Turn.NOUGHT
                bindingMain.lblP2.background = null
                bindingMain.lblP1.background = originalBackgroundP1
                break
            }else{
                continue
            }
        }

    }

    // Função auxiliar para verificar se o botão contêm o mesmo símbolo
    private fun match(button: Button, symbol: String): Boolean = button.text == symbol

    // Verifica se há uma vitória com base no símbolo
    private fun checkForVictory(s: String): Boolean {

        // Verifica vitória na horizontal
        if(match(bindingBoard.a1, s) && match(bindingBoard.a2, s) && match(bindingBoard.a3, s)){
            utils.paintBackgroundColor(
                listOf(bindingBoard.a1, bindingBoard.a2, bindingBoard.a3)
            )
            return true
        }
        if(match(bindingBoard.b1, s) && match(bindingBoard.b2, s) && match(bindingBoard.b3, s)){
            utils.paintBackgroundColor(
                listOf(bindingBoard.b1, bindingBoard.b2, bindingBoard.b3)
            )
            return true

        }
        if(match(bindingBoard.c1, s) && match(bindingBoard.c2, s) && match(bindingBoard.c3, s)){
            utils.paintBackgroundColor(
                listOf(bindingBoard.c1, bindingBoard.c2, bindingBoard.c3)
            )
            return true
        }


        // Verifica vitória na vertical
        if(match(bindingBoard.a1, s) && match(bindingBoard.b1, s) && match(bindingBoard.c1, s)){
            utils.paintBackgroundColor(
                listOf(bindingBoard.a1, bindingBoard.b1, bindingBoard.c1)
            )
            return true
        }
        if(match(bindingBoard.a2, s) && match(bindingBoard.b2, s) && match(bindingBoard.c2, s)){
            utils.paintBackgroundColor(
                listOf(bindingBoard.a2, bindingBoard.b2, bindingBoard.c2)
            )
            return true
        }
        if(match(bindingBoard.a3, s) && match(bindingBoard.b3, s) && match(bindingBoard.c3, s)){
            utils.paintBackgroundColor(
                listOf(bindingBoard.a3, bindingBoard.b3, bindingBoard.c3)
            )
            return true
        }

        // Verifica vitória na diagonal
        if(match(bindingBoard.a1, s) && match(bindingBoard.b2, s) && match(bindingBoard.c3, s)){
            utils.paintBackgroundColor(
                listOf(bindingBoard.a1, bindingBoard.b2, bindingBoard.c3)
            )
            return true
        }
        if(match(bindingBoard.a3, s) && match(bindingBoard.b2, s) && match(bindingBoard.c1, s)){
            utils.paintBackgroundColor(
                listOf(bindingBoard.c1, bindingBoard.b2, bindingBoard.a3)
            )
            return true
        }


        return false
    }

    // Mostra o resultado do jogo em um Toast e permite reiniciar o jogo
    private fun result(title: String) {

        Toast.makeText(context, title, Toast.LENGTH_SHORT).show()

        bindingMain.btnResetGame.visibility = View.VISIBLE

        bindingMain.btnResetGame.setOnClickListener {
            resetBoard()
        }

    }

    // Salva o estado atual do jogo no banco de dados
    private fun saveGameState(){

        val adapter = GameStateAdapter(context)

        if(adapter.matchExists(matchId)){
            adapter.deleteMatch(matchId)
        }

        val newGameState = GameState(matchId,
                                    bindingMain.lblP1.text.toString(),
                                    bindingMain.p1Score.text.toString(),
                                    bindingMain.lblP2.text.toString(),
                                    bindingMain.p2Score.text.toString(),
                                    bindingMain.drawScore.text.toString(),
                                    currentTurn,
                                    createdAt
        )

        adapter.insertGameState(newGameState)

    }

    // Reinicia o tabuleiro e permite jogar novamente
    private fun resetBoard() {
        for(button in boardList){
            button.text = ""
            utils.blankBackgroundColor(button)
        }

        gameStopped = false

        bindingMain.btnResetGame.visibility = View.GONE

        // Alterna o turno do primeiro jogador
        firstTurn = if(firstTurn == Turn.NOUGHT)
            Turn.CROSS else Turn.NOUGHT

        // Se o oponente for um robô e for a vez do robô jogar, o robô faz sua jogada
        if(isRobot && currentTurn == Turn.CROSS){
                handleBotMove()
        }

    }

}