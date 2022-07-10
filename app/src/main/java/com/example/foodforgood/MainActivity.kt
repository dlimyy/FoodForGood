package com.example.foodforgood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodforgood.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.homePageGiver.setOnClickListener {
            startActivity(Intent(this, Giver::class.java))
            finish()
        }
        binding.homePageCollector.setOnClickListener {
            startActivity(Intent(this, Collector::class.java))
            finish()
        }
        binding.homePageGuide.setOnClickListener {
            startActivity(Intent(this,Guide::class.java))
            finish()
        }
        binding.logOutButton.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }

        binding.homePageLeaderboard.setOnClickListener {
            startActivity(Intent(this,LeaderBoard::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, LoginPage::class.java))
        finish()
    }
}