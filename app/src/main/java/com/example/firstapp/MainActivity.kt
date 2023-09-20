package com.example.firstapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("myFile", MODE_PRIVATE)
        val extraPriceCalculator = ExtraPriceCalculator(this, sharedPref)
        extraPriceCalculator.setOnClickListeners()
        extraPriceCalculator.loadSaveData()
        extraPriceCalculator.setRadioListener()
    }
}

