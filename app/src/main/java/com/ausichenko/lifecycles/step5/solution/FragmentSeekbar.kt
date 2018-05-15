package com.ausichenko.lifecycles.step5.solution

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.ausichenko.lifecycles.R
import com.ausichenko.lifecycles.step5.SeekBarViewModel
import kotlinx.android.synthetic.main.fragment_seekbar.view.*

class FragmentSeekbar : Fragment() {

    private var seekBar: SeekBar? = null
    private var seekBarViewModel: SeekBarViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater.inflate(R.layout.fragment_seekbar_solution, container, false)

        seekBar = root.seekBar

        seekBarViewModel = activity?.let { ViewModelProviders.of(it).get(SeekBarViewModel::class.java) }
        subscribeSeekBar()

        return root
    }

    private fun subscribeSeekBar() {
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    seekBarViewModel?.seekbarValue?.value = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }

        })

        activity?.let {
            seekBarViewModel?.seekbarValue?.observe(it, Observer<Int> {integer ->
                integer?.let { it1 -> seekBar?.setProgress(it1) }
            })
        }
    }
}