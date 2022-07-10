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
        }
        binding.homePageCollector.setOnClickListener {
            startActivity(Intent(this, Collector::class.java))
        }
    }
}