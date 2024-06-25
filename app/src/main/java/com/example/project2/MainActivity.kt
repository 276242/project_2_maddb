package com.example.project2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.project2.firestore.FireStoreClass
import com.example.project2.firestore.User
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        FireStoreClass().getUserDetails(this)


        // kontotestowedatabase@gmail.com

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

//        // button to start the results
//        findViewById<Button>(R.id.buttonResults).setOnClickListener {
//            val intent = Intent(this, ResultsActivity::class.java)
//            startActivity(intent)
//        }
    }

    fun onUserDetailsReceived(user: User) {
        findViewById<TextView>(R.id.welcomeText).text = "Welcome, ${user.name}"
    }
}