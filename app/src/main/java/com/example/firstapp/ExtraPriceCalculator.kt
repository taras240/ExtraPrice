package com.example.firstapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExtraPriceCalculator(
    private val activity: AppCompatActivity,
    private val sharedPreferences: SharedPreferences
) {
    private var vibration = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private val oneButton = activity.findViewById<Button>(R.id.oneButton)
    private val twoButton = activity.findViewById<Button>(R.id.twoButton)
    private val threeButton = activity.findViewById<Button>(R.id.threeButton)
    private val fourButton = activity.findViewById<Button>(R.id.fourButton)
    private val fiveButton = activity.findViewById<Button>(R.id.fiveButton)
    private val sixButton = activity.findViewById<Button>(R.id.sixButton)
    private val sevenButton = activity.findViewById<Button>(R.id.sevenButton)
    private val eightButton = activity.findViewById<Button>(R.id.eightButton)
    private val nineButton = activity.findViewById<Button>(R.id.nineButton)
    private val zeroButton = activity.findViewById<Button>(R.id.zeroButton)

    private val doteButton = activity.findViewById<Button>(R.id.doteButton)
    private val ceButton = activity.findViewById<Button>(R.id.clearButton)
    private val enteredPriceTextView = activity.findViewById<TextView>(R.id.myTextView)
    private val resultTextView = activity.findViewById<TextView>(R.id.resultTextView)

    private val countGroup = activity.findViewById<LinearLayout>(R.id.countGroup)
    private val enteredCountTextView = activity.findViewById<TextView>(R.id.enteredCountTextView)
    private val countHintTextView = activity.findViewById<TextView>(R.id.countTextView)

    private val percentageRadioGroup =
        activity.findViewById<RadioGroup>(R.id.percentageRadioGroup)


    private var currentPrice = .0
    private val currentPriceString = StringBuilder()
    private var newPrice = .0
    private val percentageKey = "percentage"
    private var percentage = 1
    private var countText = StringBuilder()
    private var count = 1.0
    private var activeInputField = ActiveInput.PRICE_TEXTVIEW
    private val timer = object : CountDownTimer(1500, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            if (currentPriceString.toString() != "")
                enteredPriceTextView.text = "/${currentPriceString}/"
            currentPriceString.clear()
        }
    }

    fun loadSaveData() {
        percentage = getNumber(percentageKey)
        Log.d("perc", percentage.toString())
        when (percentage) {
            105 -> activity.findViewById<RadioButton>(R.id.radio_5).isChecked = true
            120 -> activity.findViewById<RadioButton>(R.id.radio_20).isChecked = true
            125 -> activity.findViewById<RadioButton>(R.id.radio_25).isChecked = true
            130 -> activity.findViewById<RadioButton>(R.id.radio_30).isChecked = true
            156 -> activity.findViewById<RadioButton>(R.id.radio_56).isChecked = true
        }
    }

    private fun updatePrices(newDigit: Int) {
        when (activeInputField) {
            ActiveInput.PRICE_TEXTVIEW -> {
                if (currentPriceString.length > 8) return
                currentPriceString.append(newDigit)
                currentPrice = currentPriceString.toString().toDouble()
                enteredPriceTextView.text = currentPriceString
                calculateNewPrice()
            }

            ActiveInput.COUNT_TEXTVIEW -> {
                countText.append(newDigit)
                if (countText.length > 6) return
                count = countText.toString().toDouble()
                enteredCountTextView.text = countText
                calculateNewPrice()
            }
        }


    }

    private fun updatePrices(newDigitString: String) {

        when (activeInputField) {
            ActiveInput.PRICE_TEXTVIEW -> {
                if ((newDigitString == "." && currentPriceString.contains('.')) || currentPriceString.length > 8) return
                if (currentPriceString.toString() == "" && newDigitString == ".")
                    currentPriceString.append("0.")
                else currentPriceString.append(newDigitString)
                currentPrice = currentPriceString.toString().toDouble()
                enteredPriceTextView.text = currentPriceString
            }

            ActiveInput.COUNT_TEXTVIEW -> {
                if (newDigitString == "." && countText.contains('.')) return
                countText.append(newDigitString)
                if (countText.length > 6) return
                count = countText.toString().toDouble()
                enteredCountTextView.text = countText
            }
        }

    }

    private fun calculateNewPrice() {
        newPrice = currentPrice * percentage / (100.0 * count)
        resultTextView.text = String.format("%.2f", newPrice)
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= 26) {
            timer.cancel()
            timer.start()
            vibration.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    fun setRadioListener() {
        percentageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            // get the radio group checked radio button
            activity.findViewById<RadioButton>(checkedId)?.apply {
                // show the checked radio button's text in text view
                percentage = when (text) {
                    "5%" -> 105
                    "20%" -> 120
                    "25%" -> 125
                    "30%" -> 130
                    "56%" -> 156
                    else -> 100
                }
            }
            saveNumber(percentageKey, percentage)
            calculateNewPrice()
            vibrate()

        }
    }

    private fun setTextviewInactiveStyle(textView: TextView) {
        textView.setBackgroundColor(Color.parseColor("#000000"))
        textView.setTextColor(Color.parseColor("#eeeeee"))
    }

    private fun setTextviewActiveStyle(textView: TextView) {
        textView.setBackgroundColor(Color.parseColor("#ff6600"))
        textView.setTextColor(Color.parseColor("#000000"))
    }

    fun setOnClickListeners() {
        enteredPriceTextView.setOnClickListener {
            activeInputField = ActiveInput.PRICE_TEXTVIEW
            setTextviewInactiveStyle(enteredCountTextView)
            setTextviewInactiveStyle(countHintTextView)
            setTextviewActiveStyle(enteredPriceTextView)
            vibrate()

        }
        countGroup.setOnClickListener {
            setTextviewActiveStyle(enteredCountTextView)
            setTextviewActiveStyle(countHintTextView)
            setTextviewInactiveStyle(enteredPriceTextView)
            activeInputField = ActiveInput.COUNT_TEXTVIEW
            countText.clear()
            vibrate()

        }

        doteButton.setOnClickListener {
            vibrate()
            updatePrices(".")
        }
        ceButton.setOnClickListener {
            vibrate()

            currentPriceString.clear()
            currentPrice = .0
            enteredPriceTextView.text = "0"
            resultTextView.text = "0"
            count = 1.0
            enteredCountTextView.text = "1"
        }
        zeroButton.setOnClickListener {
            vibrate()
            updatePrices(0)
        }
        oneButton.setOnClickListener {
            vibrate()
            updatePrices(1)
        }
        twoButton.setOnClickListener {
            vibrate()
            updatePrices(2)
        }
        threeButton.setOnClickListener {
            vibrate()
            updatePrices(3)
        }
        fourButton.setOnClickListener {
            vibrate()
            updatePrices(4)
        }
        fiveButton.setOnClickListener {
            vibrate()
            updatePrices(5)
        }
        sixButton.setOnClickListener {
            vibrate()
            updatePrices(6)
        }
        sevenButton.setOnClickListener {
            vibrate()
            updatePrices(7)
        }
        eightButton.setOnClickListener {
            vibrate()
            updatePrices(8)
        }
        nineButton.setOnClickListener {
            vibrate()
            updatePrices(9)
        }
    }

    private fun saveNumber(key: String, number: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, number)
        editor.apply()
    }

    private fun getNumber(key: String): Int {
        return sharedPreferences.getInt(key, 120)
    }

    enum class ActiveInput {
        COUNT_TEXTVIEW,
        PRICE_TEXTVIEW
    }
}