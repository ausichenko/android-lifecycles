package com.ausichenko.lifecycles.step5

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.ausichenko.lifecycles.R
import kotlinx.android.synthetic.main.fragment_seekbar.view.*

class FragmentSeekbar : Fragment() {

    private var seekBar: SeekBar? = null
    private var seekBarViewModel: SeekBarViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater.inflate(R.layout.fragment_seekbar, container, false)

        seekBar = root.seekBar

        // TODO: get ViewModel
        subscribeSeekBar()

        return root
    }

    private fun subscribeSeekBar() {
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // TODO: Set the ViewModel's value when the change comes from the user.
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }

        })

        // TODO: Update the SeekBar when the ViewModel is changed.
        // seekBarViewModel.seekbarValue.observe(...
    }
}