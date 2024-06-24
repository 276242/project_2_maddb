package com.example.project2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputEmail = findViewById(R.id.inputEmailL)
        inputPassword = findViewById(R.id.inputPasswordL)
        loginButton = findViewById(R.id.loginButton)

        loginButton?.setOnClickListener {
            logInRegisteredUser()
        }

        val registerTextView: TextView = findViewById(R.id.registerTextView)
        registerTextView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.registerTextView -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(inputEmail?.text.toString().trim { it <= ' ' }) -> {
                // showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(inputPassword?.text.toString().trim { it <= ' ' }) -> {
                // showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                // showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            val email = inputEmail?.text.toString().trim { it <= ' ' }
            val password = inputPassword?.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // showErrorSnackBar("You are logged in successfully.", false)
                        goToMainActivity()
                        finish()
                    } else {
                        // showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun goToMainActivity() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.email.toString()
        val name = user?.displayName.toString()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uID", uid)
        intent.putExtra("name", name)
        startActivity(intent)
    }
}