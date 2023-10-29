package com.daedrii.tictactoe.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.daedrii.tictactoe.controller.GameController
import com.daedrii.tictactoe.R
import com.daedrii.tictactoe.controller.AppUtils
import com.daedrii.tictactoe.databinding.ActivityMainBinding
import com.daedrii.tictactoe.databinding.FragmentBoardBinding
import com.daedrii.tictactoe.model.GameState
import com.daedrii.tictactoe.model.Turn


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var utils: AppUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        utils = AppUtils(applicationContext)

        val boardFragment = supportFragmentManager.findFragmentById(R.id.main_board_container) as BoardFragment

        boardFragment.bindingReady.observe(this) { isReady ->
            if (isReady)
                startGame(boardFragment.getBinding())

        }

    }

    private fun startGame(boardFragmentBinding: FragmentBoardBinding) {
        val game = GameController(this@MainActivity, this.binding, boardFragmentBinding)
        val match = extractMatchFromIntent()

        match?.let {
            game.initGame(it)
        }

        binding.btnLeaveGame.setOnClickListener {
            game.leaveGame()
        }
    }

    private fun extractMatchFromIntent(): GameState? {
        val id = intent.extras?.getLong("matchId").toString().toLong()
        val p1 = intent.extras?.getString("p1Name").toString()
        val p2 = intent.extras?.getString("p2Name").toString()
        val p1Score = intent.extras?.getString("p1Score").toString()
        val p2Score = intent.extras?.getString("p2Score").toString()
        val drawScore = intent.extras?.getString("drawScore").toString()
        val currentTurn = intent.getSerializableExtra("currentTurn") as Turn //TODO lidar com deprecated. https://stackoverflow.com/questions/72571804/getserializableextra-and-getparcelableextra-are-deprecated-what-is-the-alternat
        val createdAt = intent.extras?.getString("createdAt")

        val createdAtDate = utils.parseCreatedAtDate(createdAt)

        return createdAtDate?.let {
            GameState(id, p1, p1Score, p2, p2Score, drawScore, currentTurn, it)
        }
    }

}