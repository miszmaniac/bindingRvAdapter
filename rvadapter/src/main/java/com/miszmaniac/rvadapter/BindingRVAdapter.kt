package com.miszmaniac.rvadapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class BindingRVAdapter(useStableIds: Boolean = true) :
    RecyclerView.Adapter<BindingAdapterHolder<ViewDataBinding>>() {

    init {
        setHasStableIds(useStableIds)
    }

    var data: List<Any> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @PublishedApi
    internal var creators = mutableMapOf<TypeResolver<*>, ViewBinder<ViewDataBinding, Any>>()

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
        return creators[getCreatorKey(position)]!!.viewId
    }

    private fun getCreatorKey(position: Int): TypeResolver<*> {
        val itemType = getItem(position)!!::class.java
        return creators.keys.firstOrNull {
            it.type == itemType && (it as TypeResolver<Any>).condition(
                getItem(position) as Any
            )
        }
            ?: throw IllegalStateException("No binding registered for type: ${itemType.simpleName}")
    }

    override fun getItemId(position: Int): Long = data[position].hashCode().toLong()

    override fun onBindViewHolder(holder: BindingAdapterHolder<ViewDataBinding>, position: Int) {
        creators[getCreatorKey(position)]!!.bind(holder.binding, getItem(position)!!)
    }

    fun getItem(position: Int): Any? =
        if (data.lastIndex < position || position < 0) null else data[position]

    override fun getItemCount(): Int = data.size

    inline fun <LayoutBindingClass : ViewDataBinding, reified DataType> register(
        @LayoutRes layoutRes: Int, noinline bind: LayoutBindingClass.(data: DataType) -> Unit
    ): BindingRVAdapter {
        @Suppress("UNCHECKED_CAST")
        creators[TypeResolver<DataType>(DataType::class.java)] =
            ViewBinder(layoutRes, bind) as ViewBinder<ViewDataBinding, Any>
        return this
    }

    inline fun <LayoutBindingClass : ViewDataBinding, reified DataType> register(
        @LayoutRes layoutRes: Int, noinline condition: (data: DataType) -> Boolean,
        noinline bind: LayoutBindingClass.(data: DataType) -> Unit
    ): BindingRVAdapter {
        @Suppress("UNCHECKED_CAST")
        creators[TypeResolver<DataType>(DataType::class.java, condition)] =
            ViewBinder(layoutRes, bind) as ViewBinder<ViewDataBinding, Any>
        return this
    }
}