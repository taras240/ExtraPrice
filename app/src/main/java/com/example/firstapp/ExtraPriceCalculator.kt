package com.example.firstapp

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.databinding.ActivityMainBinding

class ExtraPriceCalculator(
    private val activity: AppCompatActivity,
    private val sharedPreferences: SharedPreferences,
    private val binding: ActivityMainBinding
) {
    val VIBRATE_DURATION_MILISEC = 50L

    private var vibration = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

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
                binding.enteredPriceTextView.text = "/${currentPriceString}/"
            currentPriceString.clear()
        }
    }

    fun loadSaveData() = with(binding) {
        percentage = getNumber(percentageKey)
        Log.d("perc", percentage.toString())
        when (percentage) {
            105 -> {
                radio5.isChecked = true
                percentTextView.text = "+5%"
            }

            120 -> {
                radio20.isChecked = true
                percentTextView.text = "+20%"
            }

            125 -> {
                radio25.isChecked = true
                percentTextView.text = "+25%"
            }

            130 -> {
                radio30.isChecked = true
                percentTextView.text = "+30%"
            }

            156 -> {
                radio56.isChecked = true
                percentTextView.text = "+56%"
            }
        }
    }

    private fun updatePrices(newDigit: Int) {
        when (activeInputField) {
            ActiveInput.PRICE_TEXTVIEW -> {
                if (currentPriceString.length > 8) return
                currentPriceString.append(newDigit)
                currentPrice = currentPriceString.toString().toDouble()
                binding.enteredPriceTextView?.text = currentPriceString
                calculateNewPrice()
            }

            ActiveInput.COUNT_TEXTVIEW -> {
                countText.append(newDigit)
                if (countText.length > 6) return
                count = countText.toString().toDouble()
                binding.enteredCountTextView.text = countText
                calculateNewPrice()
            }
        }


    }

    private fun updatePrices(newDigitString: String) = with(binding) {
        when (activeInputField) {
            ActiveInput.PRICE_TEXTVIEW -> {
                if ((newDigitString == "." && currentPriceString.contains('.')) || currentPriceString.length > 8) return
                if (currentPriceString.toString() == "" && newDigitString == ".")
                    currentPriceString.append("0.")
                else currentPriceString.append(newDigitString)
                currentPrice = currentPriceString.toString().toDouble()
                enteredPriceTextView?.text = currentPriceString
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
        binding.resultTextView.text = "=${String.format("%.2f", newPrice)}"
    }

    private fun vibrate() {

        timer.cancel()
        timer.start()
        vibration.vibrate(
            VibrationEffect.createOneShot(
                VIBRATE_DURATION_MILISEC,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )

    }

    fun setRadioListener() = with(binding) {
        percentageRadioGroup.setOnCheckedChangeListener { _, _ ->
            percentage = when {
                radio5.isChecked -> {
                    percentTextView.text = "+5%"
                    105
                }

                radio20.isChecked -> {
                    percentTextView.text = "+20%"
                    120
                }

                radio25.isChecked -> {
                    percentTextView.text = "+25%"
                    125
                }

                radio30.isChecked -> {
                    percentTextView.text = "+30%"
                    130
                }

                radio56.isChecked -> {
                    percentTextView.text = "+56%"
                    156
                }

                else -> 100
            }
            saveNumber(percentageKey, percentage)
            calculateNewPrice()
            vibrate()
        }
    }

    private fun setTextviewInactiveStyle(textView: TextView) {
        textView.setBackgroundResource(R.color.black)
        textView.setTextColor(activity.resources.getColor(R.color.mainTextColor))
    }

    private fun setTextviewActiveStyle(textView: TextView) {
        textView.setBackgroundResource(R.color.mainColor)
        textView.setTextColor(activity.resources.getColor(R.color.black))
    }
    private fun setEnteredPriceFieldActive()= with(binding){
        activeInputField = ActiveInput.PRICE_TEXTVIEW
        setTextviewInactiveStyle(enteredCountTextView)
        setTextviewInactiveStyle(countTextView)
        setTextviewActiveStyle(enteredPriceTextView)
        setTextviewActiveStyle(percentTextView)
    }
    private fun setEnteredPercentFieldActive() =with(binding){
        setTextviewActiveStyle(enteredCountTextView)
        setTextviewActiveStyle(countTextView)
        setTextviewInactiveStyle(enteredPriceTextView)
        setTextviewInactiveStyle(percentTextView)

        activeInputField = ActiveInput.COUNT_TEXTVIEW
        countText.clear()
    }
    fun setOnClickListeners()  = with(binding){
        enteredPriceTextView.setOnClickListener {
            setEnteredPriceFieldActive()
            vibrate()
        }
        percentTextView.setOnClickListener {
            setEnteredPriceFieldActive()
            vibrate()
        }
        countGroup.setOnClickListener {
            setEnteredPercentFieldActive()
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