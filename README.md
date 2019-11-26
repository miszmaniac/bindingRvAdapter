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

recyclerView.adapter = adapter

adapter.data = (0..2000).map { it.toString() }
```

## How to start

1. **Step 1.** Add the JitPack repository to your build file
```groovy
allprojects {
    repositories {
        ...        
        maven { url 'https://jitpack.io' }
    }
}
```
2. **Step 2.** Add the dependency
```groovy
dependencies {
    implementation 'com.github.miszmaniac:bindingRvAdapter:1.2.1'
}
```
3. **Step 3.** Enable data binding in your app module build.gradle file

```groovy
// Enable kapt to generate binding classes
apply plugin: 'kotlin-kapt'

// Enable data binding
android {
    ...
    dataBinding {
        enabled = true
    }
}

```

4. **Step 4.** Wrap your RecyclerView item in <layout> tag (without it Bindings are not generated) 
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">
        
        ...
        
    </LinearLayout>

</layout>
```

5. **Step 5.** Create adapter and start registering your data.

View binding generates classes with the same name as your Layout file: 
`test_item_layout -> TestItemLayoutBinding`

First generic type for register function is your Binding class and second is type of data that you want to assign with this view type.

```kotlin
val adapter = BindingRVAdapter()
    .register<TestItemLayoutBinding, String>(R.layout.test_item_layout) { data ->
        title.text = data
    }
    .register<TestItemLayoutBinding, Int>(R.layout.test_item_layout) { data ->
        title.text = context.getString(data)
    }
```

6. **Step 6.** Update RecyclerView data
```kotlin
    adapter.data = listOf("Cat", "Dog", "Fish", R.string.bird)
```

7. **Step 7.** This is it... just run your app now:)
