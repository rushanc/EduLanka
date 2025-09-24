package com.example.edulanka

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailField: EditText = findViewById(R.id.regEmail)
        val passwordField: EditText = findViewById(R.id.regPassword)
        val confirmField: EditText = findViewById(R.id.regConfirmPassword)
        val roleGroup: RadioGroup = findViewById(R.id.regRoleGroup)
        val registerButton: Button = findViewById(R.id.btnRegister)
        val loginLink: TextView = findViewById(R.id.linkLogin)

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirm = confirmField.text.toString().trim()
            val selectedRoleId = roleGroup.checkedRadioButtonId

            if (email.isEmpty() || password.isEmpty() || confirm.isEmpty() || selectedRoleId == -1) {
                Toast.makeText(this, "Please fill all fields and select a role.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirm) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val role = findViewById<RadioButton>(selectedRoleId).text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registered as $role", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("ROLE_PREFILL", role)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, task.exception?.localizedMessage ?: "Registration failed", Toast.LENGTH_LONG).show()
                    }
                }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
