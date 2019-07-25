package com.miszmaniac.bindingrvadapter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.miszmaniac.bindingrvadapter.databinding.TestItemLayoutBinding
import com.miszmaniac.rvadapter.BindingRVAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BindingRVAdapter()
            .register<TestItemLayoutBinding, String>(R.layout.test_item_layout) { data ->
                title.text = data
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT).show()
                }
            }
            .register<TestItemLayoutBinding, Int>(R.layout.test_item_layout) { data ->
                title.text = getString(data)
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, getString(data), Toast.LENGTH_SHORT).show()
                }
            }
            .apply {
                data = (0..2000).map { it.toString() }
                setHasStableIds(true)
            }
        recyclerView.adapter = adapter
    }
}
