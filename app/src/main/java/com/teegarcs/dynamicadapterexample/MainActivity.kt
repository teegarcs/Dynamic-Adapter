package com.teegarcs.dynamicadapterexample

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.teegarcs.dynamicadapter.DynamicAdapter
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), ClickLabelAction {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.sampleRecyclerView.layoutManager = LinearLayoutManager(this)
        val list = ArrayList<DynamicModel>()

        for (i in 0..10) {
            list.add(SampleLabelModel("Row Number $i"))
        }

        for (i in 0..20) {
            list.add(SampleClickableLabelModel("Row Number $i"))
        }


        // Demo of the items being updated leveraging LD and not item changed called.
        Handler().postDelayed({
            list.forEachIndexed { index, dynamicBindingModel ->
                (dynamicBindingModel as SampleLabelModel).labelLD.value = "Row Number Update $index"
            }
        }, 5000)

        val adapter = DynamicAdapter(this, items = list)
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
