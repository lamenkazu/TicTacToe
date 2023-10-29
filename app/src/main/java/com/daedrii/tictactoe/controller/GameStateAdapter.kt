package com.daedrii.tictactoe.controller

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.daedrii.tictactoe.R
import com.daedrii.tictactoe.model.GameState
import com.daedrii.tictactoe.model.GameStateDAO

class GameStateAdapter(private val context: Context): BaseAdapter() {

    private val stateList: MutableList<GameState> = ArrayList()
    private val gameStateDAO = GameStateDAO(context)

    init{
        updateGameStateList()
    }

    // Retorna o número de elementos na lista
    override fun getCount(): Int {
        return this.stateList.size
    }

    // Retorna o item que representa a match na posição especificada
    override fun getItem(position: Int): Any {
        return  this.stateList[position]
    }

    // Retorna o ID do item na posição especificada
    override fun getItemId(position: Int): Long {
        return this.stateList[position].id
    }

    // Cria a visualização de um item na lista
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val gameStateListView: View

        if (convertView == null) {
            // Cria uma nova visualização se necessário
            gameStateListView = LayoutInflater
                .from(context)
                .inflate(R.layout.item_gamestate_list,
                    parent, false)

            holder = ViewHolder(gameStateListView)
            gameStateListView.tag = holder

        } else {
            // Reutiliza a visualização existente se possível
            gameStateListView = convertView
            holder = convertView.tag as ViewHolder

        }

        val current = stateList[position]

        // Define os valores dos TextViews com base nos dados do GameState
        holder.txtGameStateId.text = current.id.toString()
        holder.txtGameStateP1Name.text = current.p1
        holder.txtGameStateP1Score.text = current.p1Score
        holder.txtGameStateP2Name.text = current.p2
        holder.txtGameStateP2Score.text = current.p2Score
        holder.txtGameStateDrawScore.text = current.drawScore

        return gameStateListView
    }

    // Atualiza a lista de estados de jogo a partir do banco de dados
    fun updateGameStateList() {
        stateList.clear()
        stateList.addAll(gameStateDAO.getAll())
        Log.d("teste", stateList.toString())
        notifyDataSetChanged()
    }

    // Exclui um match com base no ID
    fun deleteMatch(id: Long) {
        gameStateDAO.deleteById(id)
        updateGameStateList()
    }

    // Verifica se um jogo com o ID fornecido existe no banco de dados
    fun matchExists(id: Long): Boolean{
        val exists = gameStateDAO.checkIfMatchExists(id)
        updateGameStateList()
        return exists
    }

    // Insere um novo GameState no banco de dados
    fun insertGameState(newProduct: GameState) {
        gameStateDAO.insert(newProduct)
        updateGameStateList()
    }

    // Classe interna para armazenar as visualizações dos elementos da lista
    private class ViewHolder(view: View) {
        val txtGameStateId: TextView = view.findViewById(R.id.id)
        val txtGameStateP1Name: TextView = view.findViewById(R.id.p1)
        val txtGameStateP1Score: TextView = view.findViewById(R.id.p1_score)
        val txtGameStateP2Name: TextView = view.findViewById(R.id.p2)
        val txtGameStateP2Score: TextView = view.findViewById(R.id.p2_score)
        val txtGameStateDrawScore: TextView = view.findViewById(R.id.draw_score)
    }

}