package com.teegarcs.dynamicadapterexample

import android.os.Bundle
import android.widget.Toast
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
        // observe the activity lifecycle, this is used for the adapter lifecycle to supplement the view
        lifecycle.addObserver(adapter)

        // observe the list data and set to the adapter as it changes
        viewModel.listContent.observe(this) {
            adapter.submitList(it)
        }

        // observe the toast message and display to user
        viewModel.showToast.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}
