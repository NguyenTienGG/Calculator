package com.example.caculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.ContentInfoCompat.Flags
import com.example.caculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var canAddoperation = false
    private var canAddDecimal = true

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    binding.workingsTV.append(view.text)
                canAddDecimal = false
            } else
                binding.workingsTV.append(view.text)
            canAddoperation = true
        }

    }

    fun operatorAction(view: View) {
        if (view is Button && canAddoperation) {
            binding.workingsTV.append(view.text)
            canAddoperation = false
            canAddDecimal = true
        }

    }

    fun allClearAction(view: View) {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
    }

    fun backSpaceAction(view: View) {
        val length = binding.workingsTV.length()
        if (length > 0)
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
    }

    fun equalAction(view: View)
    {
        binding.resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty() ) return ""

        val timesDivision = timeDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty() ) return ""

        val result = addSubtractCalculate(timesDivision)

        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float
        for (i in passedList.indices )
        {
            if (passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator =="+")
                    result += nextDigit
                if (operator =="-")
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timeDivisionCalculate(passList: MutableList<Any>): MutableList<Any>
    {
        var list = passList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return  list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()

        var restartIndex = passedList.size

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex )
            {
                val operator = passedList[i]
                val prevDigit = passedList[i -1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x'->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/'->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if( i > restartIndex)
                newList.add(passedList[i])

        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in binding.workingsTV.text)
        {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}