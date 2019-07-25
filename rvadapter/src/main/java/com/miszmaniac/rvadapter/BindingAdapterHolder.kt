package com.miszmaniac.rvadapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingAdapterHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)