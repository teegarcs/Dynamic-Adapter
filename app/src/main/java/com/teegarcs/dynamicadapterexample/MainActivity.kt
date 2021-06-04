package com.teegarcs.dynamicadapterexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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

        //binding.sampleRecyclerView.addItemDecoration(DynamicSectionHeader(true))
//        binding.sampleRecyclerView.
//        val itemTouchHelper = ItemTouchHelper(mIth)
        mIth.attachToRecyclerView(binding.sampleRecyclerView)
        // observe the list data and set to the adapter as it changes
        viewModel.listContent.observe(this) {
            adapter.submitList(it)
        }

        // observe the toast message and display to user
        viewModel.showToast.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

    }

//    val itemCallback = ItemTouchHelper.SimpleCallback(
//    ItemTouchHelper.UP or ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0)

    val mIth = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder
            ): Boolean {
                val fromPos = viewHolder.bindingAdapterPosition
                val toPos = target.bindingAdapterPosition

                viewModel.swapPosition(fromPos, toPos)
//                (recyclerView.adapter as? DynamicAdapter)?.let {
//                    it.currentList.moveItem(fromPos, toPos)
//                }
                //recyclerView.adapter?.notifyItemMoved(fromPos, toPos)
                return true // true if moved, false otherwise
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                // remove from adapter
                viewModel.removeItemPosition(viewHolder.bindingAdapterPosition)
            }
        })
}
