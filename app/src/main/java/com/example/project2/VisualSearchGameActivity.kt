package com.example.project2

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class VisualSearchGameActivity : AppCompatActivity() {

    private lateinit var textViewLetterAbove: TextView
    private lateinit var gridViewLetters: GridView
    private lateinit var buttonStart: Button
    private lateinit var textViewTimer: TextView

    private val letters = ('A'..'Z').toList()
    private var correctLetter: Char = 'A'
    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var totalAnswers = 0

    private lateinit var totalAttemptsTextView: TextView
    private lateinit var correctAnswersTextView: TextView
    private lateinit var wrongAnswersTextView: TextView

    private lateinit var countdownTimer: CountDownTimer
    private val gameDurationSeconds = 60

    private lateinit var backButton: ImageButton
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_search_game)

        textViewLetterAbove = findViewById(R.id.textViewLetterAbove)
        gridViewLetters = findViewById(R.id.gridViewLetters)
        buttonStart = findViewById(R.id.buttonStart)
        textViewTimer = findViewById(R.id.textViewTimer)

        totalAttemptsTextView = findViewById(R.id.totalAttemptsTextView)
        correctAnswersTextView = findViewById(R.id.correctAnswersTextView)
        wrongAnswersTextView = findViewById(R.id.wrongAnswersTextView)

        backButton = findViewById(R.id.backButton3)

        backButton.setOnClickListener {
            showBackDialogue()
        }

        buttonStart.setOnClickListener {
            startGame()
        }

        gridViewLetters.visibility = View.INVISIBLE
        textViewLetterAbove.visibility = View.INVISIBLE
        textViewTimer.visibility = View.INVISIBLE
        totalAttemptsTextView.visibility = View.INVISIBLE
        correctAnswersTextView.visibility = View.INVISIBLE
        wrongAnswersTextView.visibility = View.INVISIBLE
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
            resetToInitialState()
        }
        builder.setNeutralButton("Cancel", null)
        builder.show()
    }

    private fun startGame() {
        buttonStart.visibility = View.INVISIBLE
        textViewLetterAbove.visibility = View.VISIBLE
        gridViewLetters.visibility = View.VISIBLE
        textViewTimer.visibility = View.VISIBLE
        totalAttemptsTextView.visibility = View.VISIBLE
        correctAnswersTextView.visibility = View.VISIBLE
        wrongAnswersTextView.visibility = View.VISIBLE

        resetGame()

        countdownTimer = object : CountDownTimer((gameDurationSeconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                textViewTimer.text = "Time left: $secondsLeft seconds"
            }

            override fun onFinish() {
                textViewTimer.text = "Time's up!"
                disableGridClicks()
                showEndGameDialog()
            }
        }.start()
    }

    private fun resetGame() {
        correctAnswers = 0
        incorrectAnswers = 0
        totalAnswers = 0
        textViewTimer.text = ""

        showNextLetterAndGrid()
    }

    private fun showNextLetterAndGrid() {
        // Select a random letter from letters list
        correctLetter = letters.random()

        // Set letter above grid
        textViewLetterAbove.text = correctLetter.toString()

        // Generate a list of random letters including the correct one
        val letterGrid = MutableList(25) { letters.random() }
        if (!letterGrid.contains(correctLetter)) {
            letterGrid[Random().nextInt(25)] = correctLetter
        }

        // Setup GridView adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, letterGrid)
        gridViewLetters.adapter = adapter

        // Handle item click on GridView
        gridViewLetters.setOnItemClickListener { parent, view, position, id ->
            val selectedLetter = parent.getItemAtPosition(position) as Char
            totalAnswers++

            if (selectedLetter == correctLetter) {
                correctAnswers++
            }
            else {
                incorrectAnswers++
            }

            totalAttemptsTextView.text = "Total Attempts: $totalAnswers"
            correctAnswersTextView.text = "Correct Answers: $correctAnswers"
            wrongAnswersTextView.text = "Wrong Answers: $incorrectAnswers"

            // Show next letter and grid
            showNextLetterAndGrid()
        }
    }

    private fun disableGridClicks() {
        gridViewLetters.setOnItemClickListener(null)
    }

    private fun showEndGameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over")
        builder.setMessage("You found $correctAnswers / $totalAnswers correct letters!")
        builder.setPositiveButton("Restart") { _, _ ->
            resetToInitialState()
        }
        builder.setNegativeButton("Exit") { _, _ ->
            goToMainActivity()
        }
        builder.setCancelable(false) // Prevent dialog from being dismissed on outside touch
        builder.show()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun resetToInitialState() {
        // Cancel the current game timer
        countdownTimer.cancel()

        // Reset game variables
        correctAnswers = 0
        totalAnswers = 0
        incorrectAnswers = 0

        // Reset visibility of views
        buttonStart.visibility = View.VISIBLE
        textViewLetterAbove.visibility = View.INVISIBLE
        gridViewLetters.visibility = View.INVISIBLE
        textViewTimer.visibility = View.INVISIBLE

        totalAttemptsTextView.visibility = View.INVISIBLE
        correctAnswersTextView.visibility = View.INVISIBLE
        wrongAnswersTextView.visibility = View.INVISIBLE

        // Reset GridView adapter
        gridViewLetters.adapter = null

        // Reset text views
        textViewLetterAbove.text = ""
        textViewTimer.text = ""
        totalAttemptsTextView.text = "Total Attempts: 0"
        correctAnswersTextView.text = "Correct Answers: 0"
        wrongAnswersTextView.text = "Wrong Answers: 0"
    }
}