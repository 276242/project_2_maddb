package com.example.project2

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project2.firestore.FireStoreClass
import com.example.project2.firestore.GameAttempt
import com.google.firebase.auth.FirebaseAuth

class MemoryGameActivity : AppCompatActivity() {

    private lateinit var buttons: Array<Button>
    private lateinit var buttonImages: Array<Int>
    private var clickedButtons = mutableListOf<Button>()
    private var matchesFound = 0
    private var totalAttempts = 0
    private var bestScore = Long.MAX_VALUE
    private var completionTime = 0L

    private lateinit var timerTextView: TextView
    private lateinit var backButton: ImageButton
    private var seconds = 0
    private var isRunning = false
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_game)

        timerTextView = findViewById(R.id.timerTextView)
        backButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            showBackDialogue()
        }

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

        buttons = Array(16) { Button(this) }
        buttonImages = arrayOf(
            R.drawable.image1, R.drawable.image1,
            R.drawable.image2, R.drawable.image2,
            R.drawable.image3, R.drawable.image3,
            R.drawable.image4, R.drawable.image4,
            R.drawable.image5, R.drawable.image5,
            R.drawable.image6, R.drawable.image6,
            R.drawable.image7, R.drawable.image7,
            R.drawable.image8, R.drawable.image8
        )

        buttonImages.shuffle()

        for (i in buttons.indices) {
            buttons[i].apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
                setBackgroundResource(R.drawable.button_background)
                setOnClickListener { onButtonClicked(this, i) }
            }
            gridLayout.addView(buttons[i])
        }

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            startGame()
        }
    }

    private fun showBackDialogue() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to exit or restart the game?")
        builder.setPositiveButton("Exit") { _, _ ->
            handler?.removeCallbacksAndMessages(null)
            goToMainActivity()
        }
        builder.setNegativeButton("Restart") { _, _ ->
            handler?.removeCallbacksAndMessages(null)
            restartGame()
        }
        builder.setNeutralButton("Cancel", null)
        builder.show()
    }

    private fun startGame() {
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.visibility = Button.GONE

        isRunning = true
        runTimer()

        showAllCards()
        Handler().postDelayed({
            hideAllCards()
        }, 3000) // Show all cards for 3 seconds
    }

    private fun showAllCards() {
        for (i in buttons.indices) {
            buttons[i].setBackgroundResource(buttonImages[i])
        }
    }

    private fun hideAllCards() {
        for (button in buttons) {
            button.setBackgroundResource(R.drawable.button_background)
        }
    }

    private fun onButtonClicked(button: Button, index: Int) {
        if (isRunning && clickedButtons.size < 2 && !clickedButtons.contains(button)) {
            button.setBackgroundResource(buttonImages[index])
            clickedButtons.add(button)

            if (clickedButtons.size == 2) {
                Handler().postDelayed({
                    checkForMatch()
                }, 400)
            }
        }
    }

    private fun checkForMatch() {
        if (buttonImages[buttons.indexOf(clickedButtons[0])] == buttonImages[buttons.indexOf(clickedButtons[1])]) {
            clickedButtons[0].isEnabled = false
            clickedButtons[1].isEnabled = false
            matchesFound++
            if (matchesFound == 8) {
                showWinDialog()
            }
        } else {
            clickedButtons[0].setBackgroundResource(R.drawable.button_background)
            clickedButtons[1].setBackgroundResource(R.drawable.button_background)
        }
        clickedButtons.clear()
    }

    private fun showWinDialog() {
        totalAttempts++
        isRunning = false
        completionTime = seconds.toLong()

        if (completionTime < bestScore) {
            bestScore = completionTime
        }

        val gameAttempt = GameAttempt(
            completionTime = completionTime,
            bestScore = bestScore,
            totalAttempts = totalAttempts
        )

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FireStoreClass().saveMemoryGameAttemptFS(
                userId = userId,
                attempt = gameAttempt
            )
        }

        if (userId != null) {
            FireStoreClass().updateGameStatsFS(
                userId = userId,
                gameType = "memoryGame",
                bestScore = bestScore.toInt(), // Ensure bestScore is stored as Int in Firestore
                totalAttempts = totalAttempts
            )
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Congratulations!")
        builder.setMessage("You've found all the matches in ${seconds} seconds!")
        builder.setPositiveButton("Restart") { _, _ ->
            restartGame()
        }
        builder.setNegativeButton("Exit") { _, _ ->
            goToMainActivity()
        }
        builder.show()
    }


    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun restartGame() {
        matchesFound = 0
        totalAttempts = 0
        seconds = 0
        bestScore = Long.MAX_VALUE

        buttonImages.shuffle()
        for (i in buttons.indices) {
            buttons[i].apply {
                setBackgroundResource(R.drawable.button_background)
                isEnabled = true
            }
        }

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.visibility = Button.VISIBLE
        isRunning = false
        timerTextView.text = "Time: 0s"

        handler?.removeCallbacksAndMessages(null) // Stop the timer
    }

    private fun runTimer() {
        handler = Handler()
        handler?.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60
                val time = String.format("%d:%02d:%02d", hours, minutes, secs)
                timerTextView.text = "Time: $time"
                if (isRunning) {
                    seconds++
                }
                handler?.postDelayed(this, 1000)
            }
        })
    }
}