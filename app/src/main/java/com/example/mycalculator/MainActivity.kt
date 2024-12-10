package com.example.mycalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var inputForCalc: TextView
    private lateinit var resultOfCalc: TextView
    private lateinit var btnAC: Button
    private lateinit var btnBackspace: Button
    private lateinit var btnBr1: Button
    private lateinit var btnBr2: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    private lateinit var btnDiv: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btnMultiply: Button
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btnMinus: Button
    private lateinit var btnDot: Button
    private lateinit var btn0: Button
    private lateinit var btnEqual: Button
    private lateinit var btnPlus: Button

    private var canAddOperation = false
    private var canAddNumber = true
    private var brCount = 0
    private var canAddBr1 = true
    private var canAddDot = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputForCalc = findViewById(R.id.inputForCalc)
        resultOfCalc = findViewById(R.id.resultOfCalc)
        btnAC = findViewById(R.id.btnAC)
        btnBackspace = findViewById(R.id.btnBackspace)
        btnBr1 = findViewById(R.id.btnBr1)
        btnBr2 = findViewById(R.id.btnBr2)
        btn7 = findViewById(R.id.btn7)
        btn8 = findViewById(R.id.btn8)
        btn9 = findViewById(R.id.btn9)
        btnDiv = findViewById(R.id.btnDiv)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)
        btn6 = findViewById(R.id.btn6)
        btnMultiply = findViewById(R.id.btnMultiply)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btnMinus = findViewById(R.id.btnMinus)
        btnDot = findViewById(R.id.btnDot)
        btn0 = findViewById(R.id.btn0)
        btnEqual = findViewById(R.id.btnEqual)
        btnPlus = findViewById(R.id.btnPlus)


    }

    fun numberAction(view: View) {
        if (view is Button && canAddNumber) {
            inputForCalc.append(view.text)
            canAddOperation = true
            canAddBr1 = false
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            inputForCalc.append(view.text)
            canAddOperation = false
            canAddBr1 = true
            canAddDot = true
            canAddNumber = true
        }

    }

    fun allClearAction(view: View) {
        inputForCalc.text = ""
        resultOfCalc.text = ""
        canAddOperation = false
        canAddNumber = true
        brCount = 0
        canAddBr1 = true
        canAddDot = true
    }

    fun backSpaceAction(view: View) {
        val len = inputForCalc.length()

        if (len > 0) {
            var str = inputForCalc.text
            when (str.last()) {
                '(' -> brCount -= 1
                ')' -> brCount += 1
            }
            inputForCalc.text = inputForCalc.text.subSequence(0, len - 1)
            str = inputForCalc.text
            if (str.length > 0 && str.last().isDigit()) {
                canAddOperation = true
                canAddNumber = true
                canAddBr1 = false
                var str1 = str
                while(str.last().isDigit() && str.length > 1) {
                    str1 = str.subSequence(0, str.length - 1)
                    if (str1.last().isDigit() && str1.length > 1) {
                        str = str1
                    } else if (str1.last() == '.') {
                        canAddDot = false
                        str = str1
                    } else {
                        canAddDot = true
                        str = str1
                    }
                }
            } else if (str.length > 0) {
                when(str.last()) {
                    '(', '+', '-', 'x', '/' -> {
                        canAddOperation = false
                        canAddNumber = true
                        canAddBr1 = true
                        canAddDot = true
                    }
                    ')' -> {
                        canAddOperation = true
                        canAddNumber = false
                        canAddBr1 = false
                        canAddDot = false
                    }
                    '.' -> {
                        canAddOperation = false
                        canAddNumber = true
                        canAddBr1 = false
                        canAddDot = true
                    }
                }
            } else {
                canAddOperation = false
                canAddNumber = true
                canAddBr1 = true
                canAddDot = true
            }
        }
    }

    fun br1Action(view: View) {
        if (view is Button && canAddBr1) {
            inputForCalc.append(view.text)
            brCount += 1
        }

    }

    fun br2Action(view: View) {
        if (view is Button && brCount > 0 && canAddOperation) {
            inputForCalc.append(view.text)
            canAddNumber = false
            brCount -= 1
        }
    }

    fun dotAction(view: View) {
        if (view is Button && canAddDot && canAddOperation) {
            inputForCalc.append(view.text)
            canAddDot = false
            canAddOperation = false
        }
    }

    fun equalsAction(view: View) {
        if (canAddBr1 == false && brCount == 0 && canAddOperation) {
            resultOfCalc.text = calculateResults()
        }
        else if (inputForCalc.length() == 0){
            resultOfCalc.text = "0"
        } else {
            resultOfCalc.text = "Wrong input"
        }

    }

    private fun calculateResults(): String {
        val listOfDigitsAndOp = digitsOperators()

        val timesDivisions = timesDivisionsCalculate(listOfDigitsAndOp)
        if (timesDivisions[0] == "Wrong input") return "Wrong input"
        val result = addSubstractCalculate(timesDivisions)

        return result.toString()
    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (ch in inputForCalc.text) {
            if(ch.isDigit() || ch == '.') currentDigit += ch
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(ch)
            }
        }

        if (currentDigit != "") list.add(currentDigit.toFloat())

        return list
    }

    private fun timesDivisionsCalculate(inputList: MutableList<Any>): MutableList<Any> {
        var list = inputList
        while (list.contains('x') || list.contains('/')) list = calcTimesDiv(list)
        return list
    }

    private fun calcTimesDiv(inputList: MutableList<Any>): MutableList<Any> {
        val list = mutableListOf<Any>()
        var resInd = inputList.size

        for (i in inputList.indices) {
            if (inputList[i] is Char && i < resInd) {
                val op = inputList[i]
                val prevDigit = inputList[i-1] as Float
                val nextDigit = inputList[i+1] as Float
                when(op) {
                    'x' ->
                    {
                        list.add(prevDigit*nextDigit)
                        resInd = i + 1
                    }
                    '/' ->
                    {
                        list.add(prevDigit/nextDigit)
                        if (nextDigit == 0f)  list[0] = "Wrong input"
                        resInd = i + 1
                    }
                    else ->
                    {
                        list.add(prevDigit)
                        list.add(op)
                    }
                }
            }

            if (i > resInd) list.add(inputList[i])
        }

        return list
    }

    private fun addSubstractCalculate(inputList: MutableList<Any>): Float {
        var res = inputList[0] as Float

        for (i in inputList.indices) {
            if (inputList[i] is Char) {
                val op = inputList[i]
                val nextDigit = inputList[i+1] as Float
                if (op == '+') res += nextDigit
                if (op == '-') res -= nextDigit
            }
        }
        return res
    }

}