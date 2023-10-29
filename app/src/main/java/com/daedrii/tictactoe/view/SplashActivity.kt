package com.daedrii.tictactoe.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.daedrii.tictactoe.R
import java.util.concurrent.Executors

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var boardList: MutableList<Button>
    private lateinit var boardFragment: BoardFragment
    private val executor = Executors.newSingleThreadExecutor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Configura a lista de botões do tabuleiro e executa a animação de splash
        setupBoardList()
        configureBoard()
        performSplashAnimation()

    }

    // Inicializa a lista de botões do tabuleiro a partir do BoardFragment
    private fun setupBoardList() {
        boardFragment = supportFragmentManager.findFragmentById(R.id.splash_board_container) as BoardFragment
        boardList = boardFragment.getBoardList()

    }

    // Configura o tabuleiro de acordo com os botões obtidos e o texto desejado
    private fun configureBoard() {

        boardFragment.bindingReady.observe(this) { isReady ->
            if (isReady) {
                val binding = boardFragment.getBinding()

                boardList.add(binding.a1)
                boardList.add(binding.a2)
                boardList.add(binding.a3)
                boardList.add(binding.b1)
                boardList.add(binding.b2)
                boardList.add(binding.b3)
                boardList.add(binding.c1)
                boardList.add(binding.c2)
                boardList.add(binding.c3)

                for (button in boardList) {
                    when (button.id) {
                        R.id.a1, R.id.a3, R.id.c1, R.id.c3 -> button.text = getString(R.string.nought)
                        R.id.a2, R.id.b1, R.id.b3, R.id.c2 -> button.text = getString(R.string.cross)
                    }
                }
            }
        }


    }

    // Realiza a animação de splash trocando o texto nos botões
    private fun performSplashAnimation() {
        executor.execute {
            for (i in 0 until 3) {
                for (button in boardList) {
                    if (button.text == getString(R.string.nought)) {
                        button.text = getString(R.string.cross)
                    } else if (button.text == getString(R.string.cross)) {
                        button.text = getString(R.string.nought)
                    }
                }

                // Aguarda um intervalo de tempo para a próxima iteração
                Thread.sleep(1200 / (i + 1).toLong())
            }
            navigateToMenuActivity()
        }
    }

    // Navega para a MenuActivity
    private fun navigateToMenuActivity() {
        val intent = Intent(applicationContext, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

}