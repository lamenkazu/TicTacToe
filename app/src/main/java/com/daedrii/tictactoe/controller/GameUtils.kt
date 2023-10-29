package com.daedrii.tictactoe.controller

import android.content.Context
import android.graphics.Color
import android.widget.Button
import com.daedrii.tictactoe.R
import com.daedrii.tictactoe.databinding.ActivityMainBinding
import com.daedrii.tictactoe.model.Turn

class GameUtils(private val context: Context, private val binding: ActivityMainBinding) {

    private var resources = context.resources

    // Obtém a string para "bolinha" do arquivo de recursos
    fun getNoughtString(): String{
        return resources.getString(R.string.nought)
    }

    // Obtém a string para "xis" do arquivo de recursos
    fun getCrossString(): String{
        return resources.getString(R.string.cross)
    }

    // Altera o rótulo de turno com base no turno atual
    fun changeTurnLabel(currentTurn: Turn) {
        binding.lblTurn.text =
            when (currentTurn) {
                Turn.NOUGHT -> "${resources.getString(R.string.turn)} ${getNoughtString()}"
                Turn.CROSS -> "${resources.getString(R.string.turn)} ${getCrossString()}"
            }
    }

    // Verifica se o tabuleiro está cheio
    fun isBoardFull(boardList: MutableList<Button>): Boolean{
        for(button in boardList){
            if(button.text.isBlank()) return false
        }

        return true
    }

    // Pinta o fundo de botões para destacar a vitória
    fun paintBackgroundColor(buttons: List<Button>){

        val txtNought = resources.getString(R.string.nought)

        for(i in 0 until buttons.size){
            //Adiciona um atraso de 1 segundo
            buttons[i].postDelayed({
                if(buttons[i].text == txtNought){
                    buttons[i].setTextColor(resources.getColorStateList(R.color.blue, context.theme))
                }else{
                    buttons[i].setTextColor(resources.getColorStateList(R.color.lightred, context.theme))
                }
            }, (50 * i).toLong())
        }
    }

    // Retorna a cor do texto do botão para a cor padrão (preto)
    fun blankBackgroundColor(button: Button){
        button.setTextColor(Color.BLACK)
    }
}