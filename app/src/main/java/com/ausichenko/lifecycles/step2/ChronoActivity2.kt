package com.ausichenko.lifecycles.step2

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import com.ausichenko.lifecycles.R
import kotlinx.android.synthetic.main.activity_chrono.*

class ChronoActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chrono)

        val chronometerViewModel : ChronometerViewModel = ViewModelProviders.of(this).get(ChronometerViewModel::class.java)

        if (chronometerViewModel.startTime == null) {
            val startTime: Long = SystemClock.elapsedRealtime()
            chronometerViewModel.startTime = startTime
            chronometer.base = startTime
        } else {
            chronometer.base = chronometerViewModel.startTime!!
        }

        chronometer.start()
    }
}