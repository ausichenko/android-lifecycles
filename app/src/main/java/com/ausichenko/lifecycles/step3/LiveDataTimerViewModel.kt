package com.ausichenko.lifecycles.step3

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.SystemClock
import java.util.*

class LiveDataTimerViewModel : ViewModel() {

    private val ONE_SECOND: Long = 1000

    var elapsedTime = MutableLiveData<Long>()

    private val initialTime: Long = SystemClock.elapsedRealtime()

    init {
        val timer = Timer()

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val newValue = (SystemClock.elapsedRealtime() - initialTime) / 1000

                // setValue() cannot be called from a background thread so post to main thread.
                //TODO post the new value with LiveData.postValue()
            }
        }, ONE_SECOND, ONE_SECOND)
    }

}