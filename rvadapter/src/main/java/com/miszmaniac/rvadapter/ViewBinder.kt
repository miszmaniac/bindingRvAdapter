package com.miszmaniac.rvadapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

data class ViewBinder<LayoutBindingClass : ViewDataBinding, DataType>(
    @LayoutRes val viewId: Int, val bind: LayoutBindingClass.(data: DataType) -> Unit
)