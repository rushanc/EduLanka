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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField: EditText = findViewById(R.id.inputEmail)
        val passwordField: EditText = findViewById(R.id.inputPassword)
        val roleGroup: RadioGroup = findViewById(R.id.roleGroup)
        val loginButton: Button = findViewById(R.id.btnLogin)
        val registerLink: TextView = findViewById(R.id.linkRegister)

        // Preselect role if coming from Register
        when (intent.getStringExtra("ROLE_PREFILL")?.lowercase()) {
            "student" -> findViewById<RadioButton>(R.id.rbStudent).isChecked = true
            "lecturer", "lecture" -> findViewById<RadioButton>(R.id.rbLecturer).isChecked = true
        }

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val selectedRoleId = roleGroup.checkedRadioButtonId
            if (email.isEmpty() || password.isEmpty() || selectedRoleId == -1) {
                Toast.makeText(this, "Please enter all fields and select a role.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val role = findViewById<RadioButton>(selectedRoleId).text.toString()

            // TODO: Replace with real authentication
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("ROLE", role)
            intent.putExtra("EMAIL", email)
            startActivity(intent)
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
