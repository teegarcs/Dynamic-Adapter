package com.teegarcs.dynamicadapterexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.teegarcs.dynamicadapter.DynamicAdapter
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import java.util.*

class MainActivity : AppCompatActivity(), ClickLabelAction {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.sampleRecyclerView.layoutManager = LinearLayoutManager(this)
        val list = ArrayList<DynamicModel>()

        for (i in 1..50) {
            if (i % 2 == 0) {
                list.add(SampleLabelModel("Standard Row: $i"))
            } else {
                list.add(SampleClickableLabelModel("Clickable Row: $i", this))
            }

        }

        // Demo of the items being updated leveraging LD and not item changed called.
        // Will only change the even rows.
        lifecycleScope.launchWhenResumed {
            delay(5000)
            list.forEachIndexed { index, dynamicBindingModel ->
                (dynamicBindingModel as? SampleLabelModel)?.labelLD?.value =
                    "Standard Row Update $index"
            }
        }

        val adapter = DynamicAdapter(list)
        binding.sampleRecyclerView.adapter = adapter
        //observe the activity lifecycle
        lifecycle.addObserver(adapter)

    }

    override fun printRow(label: String?) {
        label?.let {
            Log.i("****", it)
        }

    }
}
