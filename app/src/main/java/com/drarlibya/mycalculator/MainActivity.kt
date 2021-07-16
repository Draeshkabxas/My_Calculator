package com.drarlibya.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.drarlibya.mycalculator.databinding.ActivityMainBinding
import java.lang.ArithmeticException
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    var lastNumeric :Boolean =false
    var lastDot:Boolean =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onDigit(view: View) {
        val clickedButton =(view as Button)
        binding.tvInput.append(clickedButton.text)
        lastNumeric=true
        println("Button ${clickedButton.text} works")
    }

    fun onClear(view: View) {
        binding.tvInput.text = ""
        lastNumeric=false
        lastDot=false
    }

    fun onDecimalPoint(view: View) {
        if(lastNumeric && !lastDot){
            binding.tvInput.append(".")
            lastNumeric=false
            lastDot=true
        }
    }

    fun isOperatorAdded(value:String):Boolean {
        val operators = setOf("-","/","*","+")
        return if (value.startsWith("-")) true
        else !operators.any { value.contains(it) }

    }

    fun onOperator(view: View) {
        if(lastNumeric
            && isOperatorAdded(binding.tvInput.text.toString())) {
            binding.tvInput.append((view as Button).text)
            lastNumeric=false
            lastDot=false
        }

    }

    fun onEqual(view: View) {
          if(!lastNumeric) return

        var tvValue = binding.tvInput.text.toString()
        var prefix = ""
        try {

            if (tvValue.startsWith("-")){
                prefix="-"
                tvValue = tvValue.substring(1)
                println("The new tvValue is $tvValue")
            }
            var one =0.0
            var two =0.0
            var operator:String = findOperator(tvValue)!!
            val split = tvValue.split(operator)
            one = split[0].toDouble()
            two = split[1].toDouble()

            if (prefix.isNotEmpty())
                one = -one

            var result = when(operator) {
                "+"->one+two
                "-"->one-two
                "*"->one*two
                "/"->one/two
                else -> 0.0
            }.toString()

            result=result.removeZeroAfterDot()

            show(result)

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun findOperator(tvValue:String): String? =
        setOf("-", "+", "/", "*").find { tvValue.contains(it) }



    private fun String.removeZeroAfterDot()=
        if (this.toDouble().mod(1.0) != 0.0)
            String.format("%s", this)
        else
            String.format("%.0f", this.toFloat())



    private fun show(value:String){
        binding.tvInput.text = value
    }

}