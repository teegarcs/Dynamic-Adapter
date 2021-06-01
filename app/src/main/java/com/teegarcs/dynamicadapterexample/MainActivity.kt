package com.teegarcs.dynamicadapterexample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.teegarcs.dynamicadapter.DynamicAdapter
import com.teegarcs.dynamicadapterexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.sampleRecyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = DynamicAdapter()
        binding.sampleRecyclerView.adapter = adapter
        //observe the activity lifecycle
        lifecycle.addObserver(adapter)

        viewModel.listContent.observe(this) {
            adapter.submitList(it)
        }
    }
}
