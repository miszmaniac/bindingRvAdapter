# BindingRvAdapter

Library to use RecyclerView without need to create ViewHolders, and with type safe way to connect with layouts.

It uses Android View Binding library to create ViewHolder that can later be filled inside lambda function in register method. 

```Kotlin
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

recyclerView.adapter = adapter

adapter.data = (0..2000).map { it.toString() }
```
