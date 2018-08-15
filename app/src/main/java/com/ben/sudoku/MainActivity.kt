package com.ben.sudoku

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

/**
 * com.ben.sudoku
 *
 * @author Benhero
 * @date 2018/7/12
 */
class MainActivity : Activity() {

    private lateinit var mNumberChooseLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNumberChooseLayout = findViewById(R.id.number_choose_layout)
        initNumberChooseLayout()
    }

    private fun initNumberChooseLayout() {
        val layoutParams = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.weight = 1f
        layoutParams.leftMargin = 10
        layoutParams.rightMargin = 10
        for (i in 0..8) {
            val textView = TextView(this)
            textView.text = (i + 1).toString()
            textView.textSize = 12f
            textView.gravity = Gravity.CENTER
            textView.setTextColor(Color.WHITE)
            textView.setBackgroundColor(Color.GRAY)
            textView.setPadding(0, 20, 0, 20)
            textView.layoutParams = layoutParams
            textView.setOnClickListener { Toast.makeText(this@MainActivity, textView.text, Toast.LENGTH_SHORT).show() }
            mNumberChooseLayout.addView(textView, layoutParams)
        }
    }
}