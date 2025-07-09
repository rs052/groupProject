package com.example.groupproject

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.GestureDetector
// delete keyEvent when movement ctrls implemented
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private lateinit var caterpillar: Caterpillar
    private lateinit var leaderboard: DatabaseReference
    private lateinit var task: GameTimerTask
    private var dialog : Boolean = false
    private var emailDialog : Boolean = false
    private lateinit var detector: GestureDetector
    private var gameStart : Boolean = false


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

        var database : FirebaseDatabase = FirebaseDatabase.getInstance( )
        leaderboard = database.getReference("leaderboard")


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

        var handler = TouchHandler()
        detector = GestureDetector(this, handler)
        detector.setOnDoubleTapListener(handler)

        gameView = GameView(this, width, height, progressBar)

        caterpillar = gameView.getCaterpillar()
        mainLayout.addView(gameView)
        mainLayout.addView(progressBar)
        mainLayout.addView(seekBar)
        setContentView(mainLayout)

        var timer = Timer()
        task = GameTimerTask(this)
        timer.schedule(task, 0L, GameView.DELTA_TIME.toLong())
    }

    fun updateModel() {
        if (caterpillar.isGameOver() && !dialog) {
            dialog = true
            task.cancel()
            runOnUiThread {
                showGameOverDialog(caterpillar.getLevel())
            }
        }
        if (gameStart) {
            caterpillar.moveCaterpillar() // move caterpillar head
            gameView.updateBody() // move caterpillar body
        }
    }

    fun updateView() {
        gameView.postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            detector.onTouchEvent(event)

        }
        return super.onTouchEvent(event)
    }

    inner class TouchHandler : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (caterpillar.isGameOver()) {
                caterpillar.reset()
                gameView.resetPosition()
                gameStart = true
                gameView.postInvalidate()

                val timer = Timer()
                task = GameTimerTask(this@MainActivity)
                timer.schedule(task, 0L, GameView.DELTA_TIME.toLong())

            } else if (!gameStart) {
                gameStart = true
            }
            return true
        }
    }

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


    private fun sendEmail(score : Int, playerName : String) {
        var emailSubject : String = "Congratulations to $playerName on an Impressive game of Caterpillar"
        var emailtxt : String = "$playerName reached level $score, " +
                "with their personal best being level ${caterpillar.getBestScore()}. very impressive!"

        var mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto")
        mIntent.type = "text/plain"


        mIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        mIntent.putExtra(Intent.EXTRA_TEXT, emailtxt)

        try {
            startActivity(Intent.createChooser(mIntent, "Share using:"))
            Log.w("MainActivity", "success")
            fetchLeaderboardIntoLists()
        } catch (ex: Exception) {
            Log.e("MainActivity", "Failed to launch email intent", ex)
        }
    }

    private fun showEmailDialog(score : Int, playerName : String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Share Score?")
        builder.setMessage("Want to share your score so you can brag about it?")
        builder.setPositiveButton("Yes, share it!") { dialog, _ ->
            Log.w("MainActivity", "email time")
            dialog.dismiss()
            sendEmail(score, playerName)
        }
        builder.setNegativeButton("Nah, I'm good") { dialog, _ -> dialog.dismiss()
            fetchLeaderboardIntoLists()
        }
        builder.show()
    }

    private fun showGameOverDialog(score: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over!")
        builder.setMessage("Your score: $score \n Your Best Score ${caterpillar.getBestScore()} \n Enter your name:")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Submit") { dialog, _ ->
            val playerName = input.text.toString()
            if (playerName.isNotEmpty()) {
                leaderboard.child(playerName).setValue(score)

                 showEmailDialog(caterpillar.getLevel(), playerName) // ask for email if user enter name
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun fetchLeaderboardIntoLists() {
        leaderboard.get().addOnSuccessListener { data ->
            val namesList = ArrayList<String>()
            val scoresList = ArrayList<Int>()

            for (child in data.children) {
                val name = child.key ?: continue
                val score = child.getValue(Int::class.java) ?: 0
                namesList.add(name)
                scoresList.add(score)
            }
            showLeaderboardDialog(namesList, scoresList)
        }.addOnFailureListener { e ->
            Log.e("Firebase", "Failed to fetch leaderboard", e)
        }
    }

    private fun showLeaderboardDialog(names: ArrayList<String>, scores: ArrayList<Int>) {
        var sb : String= ""
        sb += "Leaderboard:\n"
        // Sort by score descending with zipped lists
        val combined = names.zip(scores).sortedWith(compareByDescending { it.second }).take(5)

        for ((index, data) in combined.withIndex()) {
            sb += "${index + 1}. ${data.first} : ${data.second}\n"
        }

        AlertDialog.Builder(this).setTitle("Leaderboard").setMessage(sb)
            .setPositiveButton("OK") { dialogN, _ ->
                caterpillar.reset()
                gameView.resetPosition()
                dialog = false
                dialogN.dismiss()
                gameStart = true

                val timer = Timer()
                task = GameTimerTask(this@MainActivity)
                timer.schedule(task, 0L, GameView.DELTA_TIME.toLong())
            }
            .show()
    }
}
