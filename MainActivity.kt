package com.example.groupproject

import android.R
import android.content.res.Resources
import android.os.Bundle
import android.os.CountDownTimer
// delete keyEvent when movement ctrls implemented
import android.view.KeyEvent
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SeekBar
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
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        var mainLayout: RelativeLayout = RelativeLayout(this)

        val seekBar = SeekBar(this)
        seekBar.max = 50
        seekBar.progress = 10
        seekBar.isFocusable = false
        seekBar.isFocusableInTouchMode = false
        val seekBarParams = RelativeLayout.LayoutParams(500, 300)
        seekBarParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        seekBarParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        seekBarParams.setMargins(0, 50, 0, 50)
        seekBar.layoutParams = seekBarParams
        seekBar.setOnSeekBarChangeListener(SpeedChangeListener())


        val displayMetrics = Resources.getSystem().displayMetrics
        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        val progressBar = ProgressBar(this, null, R.attr.progressBarStyleHorizontal)
        progressBar.max = 20
        progressBar.progress = 0
        val progressParams = RelativeLayout.LayoutParams(650, 200)
        progressParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        progressParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        progressParams.setMargins(0, 50, 0, 50)
        progressBar.layoutParams = progressParams


        gameView = GameView(this, width, height, progressBar)
        caterpillar = gameView.getCaterpillar()
        mainLayout.addView(gameView)
        mainLayout.addView(progressBar)
        mainLayout.addView(seekBar)
        setContentView(mainLayout)

        var timer = Timer()
        var task = GameTimerTask(this)
        timer.schedule(task, 0L, GameView.DELTA_TIME.toLong())
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

    inner class SpeedChangeListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            caterpillar.setSpeed(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    }
}