package com.miszmaniac.rvadapter

import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes

data class ViewBinder<LayoutBindingClass : ViewDataBinding, DataType>(
    @LayoutRes val viewId: Int, val bind: LayoutBindingClass.(data: DataType) -> Unit
)