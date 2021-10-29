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
        initData()
    }

    private fun initData() {
        val data = floatArrayOf(9F, -5F, 7F, -15F, -5F, -11F, -4F, -12F, -6F, -11F, 5F, 0F, -13F, 11F)
        lineChart?.setData(data)
    }

    private fun initAction() {
        buttonStart?.setOnClickListener {
            circleTemperature?.setValue(10F)
        }
    }

    private fun initView() {
        lineChart = findViewById(R.id.lineChart)
        circleTemperature = findViewById(R.id.circleTemperature)
        buttonStart = findViewById(R.id.btnStart)
    }
}