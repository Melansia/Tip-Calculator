package com.example.calculator

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    // Importing all the buttons and text views from xml file as variables
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var tvFooter: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Searching all the text vies, texts and seekbar by ID
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        tvFooter = findViewById(R.id.tvFooter)

        seekBarTip.progress = INITIAL_TIP_PERCENT

        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"

        // when we setting up our screen we calling this method with the tip description
        // so the language will be in sync with what we start off with
        updateTipDescription(INITIAL_TIP_PERCENT)
        // object expressions are example of how to create anonymous classes
        // which are one time use classes for implementing interfaces like TextWatcher
        seekBarTip.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            // adding functionality to the seekbar
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                // changing the text view of percentage textview on the go with help of
                // onProgressChanged function
                Log.i(TAG, "onProgressChanged $p1")
                // changing the Int value of the progress bar into a string with help of
                // string interpolation also adding an % sign at the end
                tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {} // not using method

            override fun onStopTrackingTouch(p0: SeekBar?) {} // not used method

        })

        tvFooter.text = "Made with ❤ for Malwarebytes"

        // passing an object which in an implementation of TextWatcher interface
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            // object expressions are example of how to create anonymous classes
            // which are one time use classes for implementing interfaces like TextWatcher

            // implementing the members
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                //p0 is the parameter passed in and is what the user is typing at that moment
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }

        })

    }

    // A method that will change the tip description based on
    // the percentage of tip that user is leaving
    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "\uD83D\uDE12"
            in 10..14 -> "\uD83D\uDE10"
            in 15..19 -> "\uD83D\uDE42"
            in 20..24 -> "\uD83D\uDE00"
            else -> "\uD83E\uDD29"
        }
        tvTipDescription.text = tipDescription
        // Update the colour based on the tipPercent
        // using the linear interpolation
        // using public method ArgbEvaluator to represent colors as Int values
//        val color = ArgbEvaluator().evaluate(
//            // we need to cast one of the fractions toFloat so we will not have a mismatch
//            // the function evaluate requires float
//            tipPercent.toFloat() / seekBarTip.max,
//            ContextCompat.getColor(this, R.color.color_worst_tip),
//            ContextCompat.getColor(this, R.color.color_best_tip)
//        ) as Int  // we return the value as an integer
//        tvTipDescription.setTextColor(color)

    }

    @SuppressLint("SetTextI18n")
    private fun computeTipAndTotal() {
        // catching a bug if the base amount is empty so that when is empty the program will not crash
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        // 1. Get the value of the base tip and percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        // 2. Compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        // 3. Update the UI to show the values
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}