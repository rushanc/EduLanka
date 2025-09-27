package com.example.edulanka

import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val goNext = Runnable {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Auto-continue after 4 seconds
        handler.postDelayed(goNext, 4000)

        // Next button: continue immediately
        findViewById<android.widget.Button>(R.id.btnNext).setOnClickListener {
            handler.removeCallbacks(goNext)
            goNext.run()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(goNext)
    }
}