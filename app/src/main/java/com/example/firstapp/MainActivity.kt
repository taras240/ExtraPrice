package com.example.firstapp

import android.app.Activity
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val view = binding.root
        setContentView(view)

        sharedPref = getSharedPreferences("myFile", MODE_PRIVATE)
        val extraPriceCalculator = ExtraPriceCalculator(this, sharedPref,binding)
        extraPriceCalculator.setOnClickListeners()
        extraPriceCalculator.loadSaveData()
        extraPriceCalculator.setRadioListener()
    }
}

