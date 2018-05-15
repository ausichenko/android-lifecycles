package com.ausichenko.lifecycles.step3

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ausichenko.lifecycles.R
import com.ausichenko.lifecycles.step3.solution.LiveDataTimerViewModel
import kotlinx.android.synthetic.main.activity_chrono_3.*

class ChronoActivity3 : AppCompatActivity() {

    private var liveDataTimerViewModel: LiveDataTimerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chrono_3)

        liveDataTimerViewModel = ViewModelProviders.of(this).get(LiveDataTimerViewModel::class.java)

        subscribe()
    }

    private fun subscribe() {
        val elapsedTimeObserver = Observer<Long> { aLong ->
            val newText = getString(R.string.seconds, aLong)
            timer_textview.text = newText
        }

        //TODO: observe the ViewModel's elapsed time
    }
}