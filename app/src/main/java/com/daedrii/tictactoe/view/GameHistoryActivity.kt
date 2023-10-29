package com.daedrii.tictactoe.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.daedrii.tictactoe.R
import com.daedrii.tictactoe.controller.AppUtils
import com.daedrii.tictactoe.controller.GameStateAdapter
import com.daedrii.tictactoe.databinding.ActivityGameHistoryBinding
import com.daedrii.tictactoe.model.GameState

class GameHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameHistoryBinding
    private lateinit var utils: AppUtils

    //Infla menu personalizado na Activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_history_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    //Define ação dos itens do menu inflado
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.help -> showQuickTip(resources.getString(R.string.quick_tip_delete_message), R.string.ans_ok)
            R.id.priority -> showQuickTip(resources.getString(R.string.quick_tip_replay_message), R.string.ans_cool)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val list = binding.gameStateList
        val adapter = GameStateAdapter(this@GameHistoryActivity)

        utils = AppUtils(applicationContext)

        // Configura a lista de visualização e os ouvintes de cliques/longos.
        setupListView(list, adapter)

    }

    // Configura a lista de visualização e os ClickListeners dos seus Itens
    private fun setupListView(list: ListView, adapter: GameStateAdapter) {
        list.adapter = adapter

        list.setOnItemClickListener { _, _, position, _ ->
            takeInfoAndStartGame(position, list)
        }

        list.setOnItemLongClickListener { _, _, position, _ ->
            askToDeleteItem(position, adapter, list)
            true
        }
    }

    // Mostra uma dica rápida em um diálogo.
    private fun showQuickTip(message: String, confirmId: Int) {
        val title = resources.getString(R.string.quick_tip_title)
        val confirm = resources.getString(confirmId)

        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(confirm) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    // Pergunta ao usuário se deseja excluir um item da lista.
    private fun askToDeleteItem(position: Int, adapter: GameStateAdapter, list: ListView) {
        val match = list.getItemAtPosition(position) as GameState

        val title = resources.getString(R.string.ask_delete_match)
        val yes = resources.getString(R.string.ans_yes)
        val no = resources.getString(R.string.ans_no)

        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton(yes) { _, _ ->
                adapter.deleteMatch(match.id)
            }
            .setNegativeButton(no) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()

    }

    // Adiciona informações extras ao intent para iniciar o jogo.
    private fun putExtrasForGame(sendNameToGameIntent: Intent, match: GameState) {
        val createdAt = utils.getCurrentTimestamp(match.createdAt)

        sendNameToGameIntent.apply {
            putExtra("matchId", match.id)
            putExtra("p1Name", match.p1)
            putExtra("p2Name", match.p2)
            putExtra("p1Score", match.p1Score)
            putExtra("p2Score", match.p2Score)
            putExtra("drawScore", match.drawScore)
            putExtra("currentTurn", match.currentTurn)
            putExtra("createdAt", createdAt)
        }
    }

    // Inicia o jogo com informações específicas da partida selecionada
    private fun takeInfoAndStartGame(position: Int, list: ListView) {

        val sendNameToGameIntent = Intent(this@GameHistoryActivity, MainActivity::class.java)
        val match = list.getItemAtPosition(position) as GameState

        putExtrasForGame(sendNameToGameIntent, match)

        startActivity(sendNameToGameIntent)
        finish()
    }



}