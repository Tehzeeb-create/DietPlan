package com.mine.dietplan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var textViewSignIn: TextView

    private val authManager = AuthenticationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        textViewSignIn = findViewById(R.id.textViewLogin)

        btnSignUp.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            if (!isValidEmail(email)) {
                Snackbar.make(it, "Please enter a valid email address", Snackbar.LENGTH_SHORT).show()
            } else if (!isPasswordValid(password)) {
                Snackbar.make(it, "Please enter a valid password (at least 8 characters with letters, numbers, and special characters)", Snackbar.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Snackbar.make(it, "Passwords do not match", Snackbar.LENGTH_SHORT).show()
            } else {
                signUp(email, password)
            }
        }
        textViewSignIn.setOnClickListener {
            // Navigate back to the LoginActivity
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        // Password must be at least 8 characters and include letters, numbers, and special characters
        val regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@$!%*?&]{8,}\$"
        return password.matches(Regex(regex))
    }

    private fun signUp(email: String, password: String) {
        authManager.signUp(email, password) { success, message ->
            if (success) {
                // Firebase sends a verification email automatically after successful signup
                authManager.sendEmailVerification() {
                    Snackbar.make(btnSignUp, "Sign up successful. Confirmation email sent.", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(btnSignUp, "Sign up failed: $message", Snackbar.LENGTH_SHORT).show()
            }
            clearInputFields()
        }
    }
    private fun clearInputFields() {
        editTextEmail.text.clear()
        editTextPassword.text.clear()
        editTextConfirmPassword.text.clear()
    }
}
