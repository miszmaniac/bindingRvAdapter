package com.miszmaniac.rvadapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BindingAdapterHolder<T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)