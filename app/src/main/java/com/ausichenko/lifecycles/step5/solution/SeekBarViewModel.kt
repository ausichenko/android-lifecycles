package com.ausichenko.lifecycles.step5.solution

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class SeekBarViewModel : ViewModel() {
    var seekbarValue = MutableLiveData<Int>()
}