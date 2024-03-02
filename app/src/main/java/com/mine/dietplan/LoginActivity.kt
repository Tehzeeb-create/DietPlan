package com.mine.dietplan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var textViewSignUp: TextView

    private val authManager = AuthenticationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        textViewSignUp = findViewById(R.id.textViewSignUp)

        btnSignIn.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(it, "Please enter both email and password", Snackbar.LENGTH_SHORT).show()
            } else {
                signIn(email, password)
            }
        }
        textViewSignUp.setOnClickListener {
            // Navigate back to the LoginActivity
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        authManager.login(email, password) { success, message ->
            if (success) {
                // Login successful, navigate to the main activity or another screen
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Login failed, show error message
                Snackbar.make(btnSignIn, "Login failed: $message", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
