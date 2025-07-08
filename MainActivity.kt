package com.example.groupproject

import android.os.Bundle
// delete keyEvent when movement ctrls implemented
import android.view.KeyEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Timer

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private lateinit var caterpillar: Caterpillar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gameView = GameView(this)
        caterpillar = gameView.getCaterpillar()
        setContentView(gameView)

        var timer = Timer()
        var task = GameTimerTask(this)
        timer.schedule(task,0L, GameView.DELTA_TIME.toLong())
    }

    fun updateModel() {
        caterpillar.moveCaterpillar() // move caterpillar head
        gameView.updateBody() // move caterpillar body
    }

    fun updateView() {
        gameView.postInvalidate()
    }

    // temp way to test caterpillar movement
    // delete when movement ctrls implemented
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> caterpillar.setDirection("up")
            KeyEvent.KEYCODE_DPAD_DOWN -> caterpillar.setDirection("down")
            KeyEvent.KEYCODE_DPAD_LEFT -> caterpillar.setDirection("left")
            KeyEvent.KEYCODE_DPAD_RIGHT -> caterpillar.setDirection("right")
        }
        return super.onKeyDown(keyCode, event)
    }
}