package com.example.photowardrobe.UI

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.photowardrobe.MainActivity
import com.example.photowardrobe.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashGif: ImageView = findViewById(R.id.splashGif)

        // Load GIF using Glide
        Glide.with(this).asGif().load(R.drawable.capture).into(splashGif)

        // Set a delay for the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the main activity after the splash screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000)
    }
}