package com.example.linechart.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.linechart.R
import com.example.linechart.ui.view.CircleTemperature
import com.example.linechart.ui.view.LineChart

class MainActivity : AppCompatActivity() {
    private var lineChart: LineChart? = null
    private var circleTemperature: CircleTemperature? = null
    private var buttonStart: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initAction()
    }

    private fun initAction() {
        buttonStart?.setOnClickListener {
            circleTemperature?.setValue(15F)
        }
    }

    private fun initView() {
        lineChart = findViewById(R.id.lineChart)
        circleTemperature = findViewById(R.id.circleTemperature)
        buttonStart = findViewById(R.id.btnStart)
    }
}