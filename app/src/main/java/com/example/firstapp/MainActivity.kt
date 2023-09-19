package com.example.firstapp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private val keyForNumber = "key_for_number"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sharedPref = getSharedPreferences("myFile", MODE_PRIVATE)
        val digits = DigitButtons(this)
        digits.setOnClickListeners()
        var counter = getNumber()

//        val plusButton = findViewById<Button>(R.id.plusButton)
//        val minusButton = findViewById<Button>(R.id.minusButton)
        val resultTextView = findViewById<TextView>(R.id.myTextView)

        resultTextView.text = "$counter"

        fun changeResultView() {
            saveNumber(counter)
            resultTextView.text = "$counter"
        }

//        plusButton.setOnClickListener {
//            counter++
//            changeResultView()
//        }

//        minusButton.setOnClickListener {
//            counter--
//            changeResultView()
//        }

    }

    class DigitButtons(val activity: AppCompatActivity) {
        val oneButton = activity.findViewById<Button>(R.id.oneButton)
        val twoButton = activity.findViewById<Button>(R.id.twoButton)
        val threeButton = activity.findViewById<Button>(R.id.threeButton)
        val fourButton = activity.findViewById<Button>(R.id.fourButton)
        val fiveButton = activity.findViewById<Button>(R.id.fiveButton)
        val sixButton = activity.findViewById<Button>(R.id.sixButton)
        val sevenButton = activity.findViewById<Button>(R.id.sevenButton)
        val eightButton = activity.findViewById<Button>(R.id.eightButton)
        val nineButton = activity.findViewById<Button>(R.id.nineButton)
        val ceButton = activity.findViewById<Button>(R.id.clearButton)
        var textView = activity.findViewById<TextView>(R.id.myTextView)

        var currentPrice = 0
        var newPrice = 0

        fun updatePrices(newDigitString: String) {
            val currentPriceString = "${currentPrice}${newDigitString}"
            Log.d("1", currentPriceString)
            if (currentPriceString.length > 8) return
            currentPrice = currentPriceString.toInt()
            textView.text = "$currentPrice"
        }

        fun setOnClickListeners() {
            ceButton.setOnClickListener {
                currentPrice = 0
                textView.text = "0"
            }
            oneButton.setOnClickListener {
                updatePrices("1")
            }
            twoButton.setOnClickListener {
                updatePrices("2")
            }
            threeButton.setOnClickListener {
                updatePrices("3")
            }
            fourButton.setOnClickListener {
                updatePrices("4")
            }
            fiveButton.setOnClickListener {
                updatePrices("5")
            }
            sixButton.setOnClickListener {
                updatePrices("6")
            }
            sevenButton.setOnClickListener {
                updatePrices("7")
            }
            eightButton.setOnClickListener {
                updatePrices("8")
            }
            nineButton.setOnClickListener {
                updatePrices("9")
            }
        }
    }


    private fun saveNumber(number: Int) {
        val editor = sharedPref.edit()
        editor.putInt(keyForNumber, number)
        editor.apply()
    }

    private fun getNumber(): Int {
        return sharedPref.getInt(keyForNumber, 0)
    }
}