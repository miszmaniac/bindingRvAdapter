package com.miszmaniac.bindingrvadapter

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.miszmaniac.bindingrvadapter.databinding.ActivityMainBinding
import com.miszmaniac.bindingrvadapter.databinding.SecondaryItemLayoutBinding
import com.miszmaniac.bindingrvadapter.databinding.TestItemLayoutBinding
import com.miszmaniac.rvadapter.BindingRVAdapter
import java.io.Serializable
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: BindingRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        adapter = BindingRVAdapter()
            .register<String, TestItemLayoutBinding>(TestItemLayoutBinding::inflate) { data ->
                title.text = data
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT).show()
                }
            }
            .register<Int, TestItemLayoutBinding>(
                TestItemLayoutBinding::inflate,
                { it % 2 == 1 }) { data ->
                title.text = "$data odd "
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, "$data odd ", Toast.LENGTH_SHORT).show()
                }
            }
            .register<Int, SecondaryItemLayoutBinding>(
                SecondaryItemLayoutBinding::inflate,
                { it % 2 == 0 }) { data ->
                title.text = "$data Even "
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, "$data Even ", Toast.LENGTH_SHORT).show()
                }
            }
            .register<Int, TestItemLayoutBinding>(TestItemLayoutBinding::inflate) { data ->
                title.text = data.toString()
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, getString(data), Toast.LENGTH_SHORT).show()
                }
            }
            .register<EnumTest, TestItemLayoutBinding>(TestItemLayoutBinding::inflate) { data ->
                title.text = "$data Enum"
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, data.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            .register<Date, TestItemLayoutBinding>(TestItemLayoutBinding::inflate) { data ->
                title.text = "${data.toGMTString()}"
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, data.toGMTString(), Toast.LENGTH_SHORT).show()
                }
            }
            .register<Serializable, TestItemLayoutBinding>(TestItemLayoutBinding::inflate) { data ->
                title.text = "$data Serializble"
                root.setOnClickListener {
                    Toast.makeText(this@MainActivity, data.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        binding.recyclerView.adapter = adapter

        adapter.data = listOf(
            SerializableTest.SERIALIZABLE_FIRST,
            SerializableTest.SERIALIZABLE_SECOND,
            "String ITEM",
            EnumTest.FIRST_ENUM,
            EnumTest.SECOND_ENUM
        ) + (0..2000).map { it }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.shuffleElements -> {
            adapter.data = adapter.data.shuffled()
            true
        }
        R.id.editElement -> {
            val index = adapter.data.indexOfFirst { it is String }
            adapter.data = adapter.data.toMutableList()
                .apply { this[index] = (this[index] as String).reversed() }.toList()
            true
        }
        R.id.addElement -> {
            adapter.data = adapter.data.toMutableList()
                .apply { this.add(0, Calendar.getInstance().time) }.toList()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}


enum class SerializableTest {
    SERIALIZABLE_FIRST, SERIALIZABLE_SECOND
}

enum class EnumTest {
    FIRST_ENUM, SECOND_ENUM
}