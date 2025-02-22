package com.example.project2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project2.firestore.FireStoreClass
import com.example.project2.firestore.GameAttempt
import com.google.firebase.auth.FirebaseAuth

class StroopTestGameActivity : AppCompatActivity() {

    private lateinit var colorName: TextView
    private lateinit var blueButton: Button
    private lateinit var blackButton: Button
    private lateinit var redButton: Button
    private lateinit var yellowButton: Button
    private lateinit var greenButton: Button
    private lateinit var startButton: Button
    private lateinit var timerTextView: TextView
    private var totalAttempts = 0
    private var bestScore = Long.MAX_VALUE
    private var correctAnswers = 0
    private var wrongAnswers = 0
    private var totalAnswers = 0

    private val colors = arrayOf("Blue", "Black", "Red", "Yellow", "Green")
    private val colorValues = mapOf(
        "Blue" to Color.BLUE,
        "Black" to Color.BLACK,
        "Red" to Color.RED,
        "Yellow" to Color.argb(255, 255, 215, 0),
        "Green" to Color.GREEN
    )

    private var currentColorName = ""
    private var currentColorValue = 0


    private lateinit var totalAnswersTextView: TextView
    private lateinit var correctAnswersTextView: TextView
    private lateinit var wrongAnswersTextView: TextView

    private lateinit var countDownTimer: CountDownTimer
    private var timerRunning = false

    private lateinit var backButton: ImageButton
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stroop_test_game)

        colorName = findViewById(R.id.colorName)
        blueButton = findViewById(R.id.blueButton)
        blackButton = findViewById(R.id.blackButton)
        redButton = findViewById(R.id.redButton)
        yellowButton = findViewById(R.id.yellowButton)
        greenButton = findViewById(R.id.greenButton)
        startButton = findViewById(R.id.startButton)
        timerTextView = findViewById(R.id.timerTextView)

        totalAnswersTextView = findViewById(R.id.totalAnswersTextView)
        correctAnswersTextView = findViewById(R.id.correctAnswersTextView)
        wrongAnswersTextView = findViewById(R.id.wrongAnswersTextView)

        backButton = findViewById(R.id.backButton2)

        backButton.setOnClickListener {
            showBackDialogue()
        }

        startButton.setOnClickListener {
            startGame()
        }
        //

        blueButton.setOnClickListener { checkAnswer(Color.BLUE) }
        blackButton.setOnClickListener { checkAnswer(Color.BLACK) }
        redButton.setOnClickListener { checkAnswer(Color.RED) }
        yellowButton.setOnClickListener { checkAnswer(Color.YELLOW) }
        greenButton.setOnClickListener { checkAnswer(Color.GREEN) }

        disableButtons()
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
            countDownTimer.cancel() // Stop the current game
            resetGame() // Reset the game variables
            showStartButton() // Show the start button again
        }
        builder.setNeutralButton("Cancel", null)
        builder.show()
    }

    private fun startGame() {
        startButton.visibility = Button.GONE
//        startButton.visibility = View.GONE
        colorName.visibility = View.VISIBLE
        blueButton.visibility = View.VISIBLE
        blackButton.visibility = View.VISIBLE
        redButton.visibility = View.VISIBLE
        yellowButton.visibility = View.VISIBLE
        greenButton.visibility = View.VISIBLE

        findViewById<TextView>(R.id.totalAnswersTextView).visibility = View.VISIBLE
        findViewById<TextView>(R.id.correctAnswersTextView).visibility = View.VISIBLE
        findViewById<TextView>(R.id.wrongAnswersTextView).visibility = View.VISIBLE

        timerTextView.visibility = View.VISIBLE // Ensure timerTextView is visible
        resetGame()

        enableButtons()

        generateNewColor()

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "Time left: $secondsLeft seconds"
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
                disableButtons()
//                showResults()
                showWinDialog()
            }
        }.start()

        timerRunning = true
    }

    private fun generateNewColor() {
        val randomColorName = colors.random()
        val randomColorValue = colorValues.values.random()

        colorName.text = randomColorName
        colorName.setTextColor(randomColorValue)

        currentColorName = randomColorName
        currentColorValue = randomColorValue
    }


    private fun checkAnswer(selectedColor: Int) {
        if (!timerRunning) return

        totalAnswers++

        if (selectedColor == currentColorValue) {
            correctAnswers++
        } else {
            wrongAnswers++
        }

        totalAnswersTextView.text = "Total Answers: $totalAnswers"
        correctAnswersTextView.text = "Correct Answers: $correctAnswers"
        wrongAnswersTextView.text = "Wrong Answers: $wrongAnswers"

        generateNewColor()
    }

    private fun showWinDialog() {
        totalAttempts++
        bestScore = correctAnswers.toLong()

        if (correctAnswers > bestScore) {
            bestScore = correctAnswers.toLong()
        }

        val gameAttempt = GameAttempt(
            correctAnswers = correctAnswers,
            wrongAnswers = wrongAnswers,
            totalAnswers = totalAnswers,
            totalAttempts = totalAttempts,
            bestScore = bestScore // Ensure bestScore is set correctly
        )

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FireStoreClass().saveStroopGameAttemptFS(
                userId = userId,
                attempt = gameAttempt
            )

            FireStoreClass().updateGameStatsFS(
                userId = userId,
                gameType = "stroopTestGame",
                bestScore = bestScore.toInt(),
                totalAttempts = totalAttempts
            )
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Time's up!")
        builder.setMessage("Your score is $correctAnswers/$totalAnswers!")
        builder.setPositiveButton("Play Again") { _, _ ->
            countDownTimer.cancel() // Stop the current game
            resetGame() // Reset the game variables
            showStartButton() // Show the start button again
        }
        builder.setNegativeButton("Exit") { _, _ ->
            goToMainActivity()
        }
        builder.show()
    }


    private fun showStartButton() {
        startButton.visibility = Button.VISIBLE
        colorName.visibility = View.INVISIBLE
        blueButton.visibility = View.INVISIBLE
        blackButton.visibility = View.INVISIBLE
        redButton.visibility = View.INVISIBLE
        yellowButton.visibility = View.INVISIBLE
        greenButton.visibility = View.INVISIBLE
        findViewById<TextView>(R.id.totalAnswersTextView).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.correctAnswersTextView).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.wrongAnswersTextView).visibility = View.INVISIBLE
        timerTextView.visibility = View.INVISIBLE
        disableButtons()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun resetGame() {
        totalAnswers = 0
        correctAnswers = 0
        wrongAnswers = 0

        totalAnswersTextView.text = "Total Answers: 0"
        correctAnswersTextView.text = "Correct Answers: 0"
        wrongAnswersTextView.text = "Wrong Answers: 0"
        timerTextView.text = ""
    }

    private fun showResults() {
        startButton.visibility = Button.VISIBLE
        timerRunning = false
    }

    private fun disableButtons() {
        blueButton.isEnabled = false
        blackButton.isEnabled = false
        redButton.isEnabled = false
        yellowButton.isEnabled = false
        greenButton.isEnabled = false

        colorName.visibility = View.INVISIBLE
        blueButton.visibility = View.INVISIBLE
        blackButton.visibility = View.INVISIBLE
        redButton.visibility = View.INVISIBLE
        yellowButton.visibility = View.INVISIBLE
        greenButton.visibility = View.INVISIBLE
        findViewById<TextView>(R.id.totalAnswersTextView).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.correctAnswersTextView).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.wrongAnswersTextView).visibility = View.INVISIBLE


    }

    private fun enableButtons() {
        blueButton.isEnabled = true
        blackButton.isEnabled = true
        redButton.isEnabled = true
        yellowButton.isEnabled = true
        greenButton.isEnabled = true

    }
}





//package com.example.strooptestgame
//
//import android.annotation.SuppressLint
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var colorName: TextView
//    private lateinit var blueButton: Button
//    private lateinit var blackButton: Button
//    private lateinit var redButton: Button
//    private lateinit var yellowButton: Button
//    private lateinit var greenButton: Button
//
//    private val colors = arrayOf("Blue", "Black", "Red", "Yellow", "Green")
//    private val colorValues = mapOf(
//        "Blue" to Color.BLUE,
//        "Black" to Color.BLACK,
//        "Red" to Color.RED,
//        "Yellow" to Color.argb(255, 255, 215, 0),
//        "Green" to Color.GREEN
//    )
//
//    private var currentColorName = ""
//    private var currentColorValue = 0
//
//    private var totalAttempts = 0
//    private var correctAnswers = 0
//    private var wrongAnswers = 0
//
//    private lateinit var totalAttemptsTextView: TextView
//    private lateinit var correctAnswersTextView: TextView
//    private lateinit var wrongAnswersTextView: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        colorName = findViewById(R.id.colorName)
//        blueButton = findViewById(R.id.blueButton)
//        blackButton = findViewById(R.id.blackButton)
//        redButton = findViewById(R.id.redButton)
//        yellowButton = findViewById(R.id.yellowButton)
//        greenButton = findViewById(R.id.greenButton)
//
//        blueButton.setOnClickListener { checkAnswer(Color.BLUE) }
//        blackButton.setOnClickListener { checkAnswer(Color.BLACK) }
//        redButton.setOnClickListener { checkAnswer(Color.RED) }
//        yellowButton.setOnClickListener { checkAnswer(Color.YELLOW) }
//        greenButton.setOnClickListener { checkAnswer(Color.GREEN) }
//
//        totalAttemptsTextView = findViewById(R.id.totalAttemptsTextView)
//        correctAnswersTextView = findViewById(R.id.correctAnswersTextView)
//        wrongAnswersTextView = findViewById(R.id.wrongAnswersTextView)
//
//        generateNewColor()
//    }
//
//    private fun generateNewColor() {
//        val randomColorName = colors.random()
//        val randomColorValue = colorValues.values.random()
//
//        colorName.text = randomColorName
//        colorName.setTextColor(randomColorValue)
//
//        currentColorName = randomColorName
//        currentColorValue = randomColorValue
//    }
//
//    private fun checkAnswer(selectedColor: Int) {
//        if (selectedColor == currentColorValue) {
//            correctAnswers++
//            totalAttempts++
////            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
//        } else {
//            wrongAnswers++
//            totalAttempts++
////            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
//        }
//
//        totalAttemptsTextView.text = "Total Attempts: $totalAttempts"
//        correctAnswersTextView.text = "Correct Answers: $correctAnswers"
//        wrongAnswersTextView.text = "Wrong Answers: $wrongAnswers"
//
//        generateNewColor()
//    }
//}