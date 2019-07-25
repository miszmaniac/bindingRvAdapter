package com.miszmaniac.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Type

class BindingRVAdapter : RecyclerView.Adapter<BindingAdapterHolder<ViewDataBinding>>() {

    var data: List<Any> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @PublishedApi
    internal var creators = mutableMapOf<Type, ViewBinder<ViewDataBinding, Any>>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingAdapterHolder<ViewDataBinding> {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return BindingAdapterHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val itemType = getItem(position)!!::class.java
        if (!creators.containsKey(itemType)) {
            throw IllegalStateException("No binding registered for type: ${itemType.simpleName}")
        }
        return creators[itemType]!!.viewId
    }

    override fun getItemId(position: Int): Long = data[position].hashCode().toLong()

    override fun onBindViewHolder(holder: BindingAdapterHolder<ViewDataBinding>, position: Int) {
        val itemType = getItem(position)!!::class.java
        creators[itemType]!!.bind(holder.binding, getItem(position)!!)
    }

    private fun getItem(position: Int): Any? =
        if (data.lastIndex < position) null else data[position]

    override fun getItemCount(): Int = data.size

    inline fun <LayoutBindingClass : ViewDataBinding, reified DataType> register(
        @LayoutRes layoutRes: Int, noinline bind: LayoutBindingClass.(data: DataType) -> Unit
    ): BindingRVAdapter {
        @Suppress("UNCHECKED_CAST")
        creators[DataType::class.java] =
            ViewBinder(layoutRes, bind) as ViewBinder<ViewDataBinding, Any>
        return this
    }
}