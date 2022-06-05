package es.yandu.mycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    private var displayText: TextView? = null
    private var clearBtn: Button? = null
    private var dotBtn: Button? = null
    private var equalsBtn: Button? = null

    /* Represent whether the lastly pressed key is numeric or not */
    private var lastIsNumeric = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayText = findViewById(R.id.displayText)
        clearBtn = findViewById(R.id.clrBtn)
        dotBtn = findViewById(R.id.dotBtn)
        equalsBtn = findViewById(R.id.equalsBtn)

        clearBtn?.setOnClickListener {
            displayText?.text = ""
            lastIsNumeric = false // Update the flag
        }

        /**
         * Append . to the TextView
         * TODO comment further
         */
        dotBtn?.setOnClickListener {
            displayText?.text?.let{
                if ( it.isEmpty() ) {
                    displayText?.append("0.")
                    lastIsNumeric = false // Update the flag
                }else if ( !it.contains(".") ) {
                    displayText?.append(".")
                    lastIsNumeric = false // Update the flag
                }
            }
        }

        /**
         * Calculate the output
         * TODO comment further
         */
        equalsBtn?.setOnClickListener {
            /* If the last input is a number only, solution can be found. */
            if( lastIsNumeric ) {
                /* Read the textView value */
                var tvText = displayText?.text.toString()
                var prefix = ""
                try {
                    /* If the value starts with '-', separate it and perform the calculation with
                     * value
                     */
                    if ( tvText.startsWith("-") ) {
                        prefix = "-"
                        tvText = tvText.substring(1)
                    }

                    when {
                        /* If the inputValue contains the Subtraction operator */
                        tvText.contains("-") ->
                            doOperation(tvText, prefix, "-")

                        /* If the inputValue contains the Addition operator */
                        tvText.contains("+") ->
                            doOperation(tvText, prefix, "+")

                        /* If the inputValue contains the Multiplication operator */
                        tvText.contains("*") ->
                            doOperation(tvText, prefix, "*")

                        /* If the inputValue contains the Division operator */
                        tvText.contains("/") ->
                            doOperation(tvText, prefix, "/")
                    }
                } catch ( e: ArithmeticException ){
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * TODO comment
     */
    fun onDigit( view: View ){
        displayText?.append((view as Button).text)
        lastIsNumeric = true // Update the flag
    }

    /**
     * Append +,-,*,/ operators to the TextView as per the Button.Text
     */
    fun onOperator( view: View ){
        var text = displayText?.text.toString()
        val operator = ( view as Button ).text

        if ( text.startsWith("-") ) {
            text = text.substring(1)
        }

        val textContainsNoOperator = !arrayOf( "/", "*", "-", "+" ).any{ it in text }

        if ( text.isEmpty() && operator == "-" ) {
            displayText?.text = "-"
        } else if ( text.isNotEmpty() && textContainsNoOperator ) {
            displayText?.append( view.text )
            lastIsNumeric = false // Update the flag
        }
    }

    /**
     * TODO comment
     */
    private fun doOperation( tvText: String, prefix: String, operator: String ) {
        var firstOperand = tvText.split( operator )[0]
        val secondOperand = tvText.split( operator )[1]
        var result = ""

        if ( prefix.isNotEmpty() ) firstOperand = prefix + firstOperand

        when ( operator ) {
            "+" ->  result = ( firstOperand.toDouble() + secondOperand.toDouble() ).toString()
            "-" ->  result = ( firstOperand.toDouble() - secondOperand.toDouble() ).toString()
            "*" ->  result = ( firstOperand.toDouble() * secondOperand.toDouble() ).toString()
            "/" ->  result = ( firstOperand.toDouble() / secondOperand.toDouble() ).toString()
        }

        /* Remove the zero after decimal point */
        if ( result.endsWith(".0") ) result = result.substring(0, result.length - 2)

        displayText?.text = result
    }

    // TODO implement negative numbers
    // TODO ability to have dot in second operand
}