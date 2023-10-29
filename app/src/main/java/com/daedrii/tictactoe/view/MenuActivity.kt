package com.daedrii.tictactoe.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.daedrii.tictactoe.R
import com.daedrii.tictactoe.controller.AppUtils
import com.daedrii.tictactoe.databinding.ActivityMenuBinding
import com.daedrii.tictactoe.model.Turn
import com.google.android.material.textfield.TextInputEditText
import java.util.Date

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    private lateinit var title: String
    private lateinit var labelStart: String
    private lateinit var labelCancel: String

    private lateinit var utils: AppUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVals() // Inicializa valores da interface do usuário
        utils = AppUtils(applicationContext)

        //Configura a ação de cada um dos botões na tela.
        setupNewGameButton()
        setupNewGameBotButton()
        setupGameHistoryButton()

    }

    // Configuraração do botão "New Game (vs Human)"
    private fun setupNewGameButton() {
        binding.newGame.setOnClickListener {

            val dialogView = createDialogView(R.string.lbl_name, true)

            val dialog = createNewGameAlertDialog(dialogView) { editTextPlayer1, editTextPlayer2 ->
                positiveActionVsHuman(editTextPlayer1, editTextPlayer2)
            }

            dialog.show()
        }
    }

    // Configuraração do botão "New Game (vs Bot)"
    private fun setupNewGameBotButton() {
        binding.newGameBot.setOnClickListener {

            val dialogView = createDialogView(R.string.lbl_name, false)

            val dialog = createNewGameAlertDialog(dialogView) { editTextPlayer1, _ ->
                positiveActionVsBot(editTextPlayer1)
            }

            dialog.show()
        }
    }

    private fun setupGameHistoryButton() {
        binding.gameHistory.setOnClickListener {
            val goToHistory = Intent(this@MenuActivity, GameHistoryActivity::class.java)
            startActivity(goToHistory)
        }
    }

    // Crie um AlertDialog para configuração do novo jogo
    private fun createNewGameAlertDialog(
        dialogView: View,
        positiveAction: (editTextPlayer1: TextInputEditText, editTextPlayer2: TextInputEditText) -> Unit
    ): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton(labelStart) { _, _ ->
                val editTextPlayer1 = dialogView.findViewById<TextInputEditText>(R.id.editTextPlayer1)
                val editTextPlayer2 = dialogView.findViewById<TextInputEditText>(R.id.editTextPlayer2)
                positiveAction(editTextPlayer1, editTextPlayer2)
            }
            .setNegativeButton(labelCancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    // Crie uma visualização personalizada para o diálogo do novo jogo
    @SuppressLint("InflateParams")
    private fun createDialogView(playerNameLabelResId: Int, showPlayer2Input: Boolean): View {
        val dialogView = layoutInflater.inflate(R.layout.dialog_new_game, null)
        val editTextPlayer1 = dialogView.findViewById<TextInputEditText>(R.id.editTextPlayer1)
        val editTextPlayer2 = dialogView.findViewById<TextInputEditText>(R.id.editTextPlayer2)
        val lblP1 = dialogView.findViewById<TextView>(R.id.lbl_p1_name)
        val lblP2 = dialogView.findViewById<TextView>(R.id.lbl_p2_name)

        lblP1.text = resources.getString(playerNameLabelResId)
        editTextPlayer1.setSelectAllOnFocus(true)

        if (!showPlayer2Input) {
            editTextPlayer2.visibility = View.GONE
            lblP2.visibility = View.GONE
        }

        return dialogView
    }

    // Ação Positiva quando o jogador joga contra o bot
    private fun positiveActionVsBot(editTextPlayer1: TextInputEditText) {

        // Obtém os valores dos inputs após o usuário inseri-los
        val p1Name = editTextPlayer1.text.toString()
        val p2Name = resources.getString(R.string.robot_name)

        val generatedId = utils.generateRandomId()
        val createdAt = utils.getCurrentTimestamp(Date())

        // Envie os dados do jogo para a próxima atividade
        val sendNameToGameIntent =
            Intent(this@MenuActivity, MainActivity::class.java).apply {
                putExtra("matchId", generatedId)
                putExtra("p1Name", p1Name)
                putExtra("p2Name", p2Name)
                putExtra("p1Score", "0")
                putExtra("p2Score", "0")
                putExtra("drawScore", "0")
                putExtra("currentTurn", Turn.NOUGHT)
                putExtra("createdAt", createdAt)
            }
        startActivity(sendNameToGameIntent)

    }

    // Ação quando o jogador joga contra outro jogador humano
    private fun positiveActionVsHuman(
        editTextPlayer1: TextInputEditText,
        editTextPlayer2: TextInputEditText) {

        val p1Name = editTextPlayer1.text.toString()
        val p2Name = editTextPlayer2.text.toString()

        val generatedId = utils.generateRandomId()
        val createdAt = utils.getCurrentTimestamp(Date())

        val sendNameToGameIntent =
            Intent(this@MenuActivity, MainActivity::class.java).apply {
                putExtra("matchId", generatedId)
                putExtra("p1Name", p1Name)
                putExtra("p2Name", p2Name)
                putExtra("p1Score", "0")
                putExtra("p2Score", "0")
                putExtra("drawScore", "0")
                putExtra("currentTurn", Turn.NOUGHT)
                putExtra("createdAt", createdAt)

            }
        startActivity(sendNameToGameIntent)
    }

    // Inicialize os valores dos elementos de interface do usuário
    private fun initVals(){
        title = resources.getString(R.string.lbl_new_game)
        labelStart = resources.getString(R.string.dialog_start)
        labelCancel = resources.getString(R.string.dialog_cancel)

    }
}