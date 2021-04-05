package com.miszmaniac.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BindingRVAdapter(useStableIds: Boolean = true) :
    RecyclerView.Adapter<BindingAdapterHolder<ViewBinding>>() {

    init {
        this.setHasStableIds(useStableIds)
    }

    var data: List<Any> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @PublishedApi
    internal var creators = mutableMapOf<TypeResolver<*>, ViewBinder<ViewBinding, Any>>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingAdapterHolder<ViewBinding> {
        val viewBinding =
            creators.values.first { it.hashCode() == viewType }
                .viewBindingFactory(LayoutInflater.from(parent.context), parent, false)
        return BindingAdapterHolder(viewBinding)
    }

    override fun getItemViewType(position: Int): Int {
        return creators[getCreatorKey(position)]!!.hashCode()
    }

    private fun getCreatorKey(position: Int): TypeResolver<*> {
        val itemType = getItem(position)!!::class.java
        return creators.keys.firstOrNull {
            it.matches(itemType, getItem(position) as Any)
        }
            ?: throw IllegalStateException("No binding registered for type: ${itemType.simpleName}")
    }

    override fun getItemId(position: Int): Long = data[position].hashCode().toLong()

    override fun onBindViewHolder(holder: BindingAdapterHolder<ViewBinding>, position: Int) {
        creators[getCreatorKey(position)]!!.bind(holder.binding, getItem(position)!!)
    }

    fun getItem(position: Int): Any? =
        if (data.lastIndex < position || position < 0) null else data[position]

    override fun getItemCount(): Int = data.size

    inline fun <reified DataType, LayoutBindingClass : ViewBinding> register(
        noinline viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> LayoutBindingClass,
        noinline bind: LayoutBindingClass.(data: DataType) -> Unit
    ): BindingRVAdapter {
        @Suppress("UNCHECKED_CAST")
        creators[TypeResolver<DataType>(DataType::class.java)] =
            ViewBinder(viewBindingFactory, bind) as ViewBinder<ViewBinding, Any>
        return this
    }

    inline fun <reified DataType, LayoutBindingClass : ViewBinding> register(
        noinline viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> LayoutBindingClass,
        noinline condition: (data: DataType) -> Boolean,
        noinline bind: LayoutBindingClass.(data: DataType) -> Unit
    ): BindingRVAdapter {
        @Suppress("UNCHECKED_CAST")
        creators[TypeResolver(DataType::class.java, condition)] =
            ViewBinder(viewBindingFactory, bind) as ViewBinder<ViewBinding, Any>
        return this
    }
}