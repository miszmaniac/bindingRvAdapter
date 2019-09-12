package com.miszmaniac.rvadapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BindingAdapterHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)