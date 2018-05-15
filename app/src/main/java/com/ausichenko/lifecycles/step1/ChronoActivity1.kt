package com.ausichenko.lifecycles.step1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ausichenko.lifecycles.R
import kotlinx.android.synthetic.main.activity_chrono.*

class ChronoActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chrono)

        chronometer.start()
    }
}