package com.example.project2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // button to start the visual search game
        findViewById<Button>(R.id.buttonVisualSearchGame).setOnClickListener {
            val intent = Intent(this, VisualSearchGameActivity::class.java)
            startActivity(intent)
        }

        // button to start the memory game
        findViewById<Button>(R.id.buttonMemoryGame).setOnClickListener {
            val intent = Intent(this, MemoryGameActivity::class.java)
            startActivity(intent)
        }

        // button to start the Stroop Test Game
        findViewById<Button>(R.id.buttonStroopTestGame).setOnClickListener {
            val intent = Intent(this, StroopTestGameActivity::class.java)
            startActivity(intent)
        }

        // button to start the results
        findViewById<Button>(R.id.buttonResults).setOnClickListener {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }
    }
}