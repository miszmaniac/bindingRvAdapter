package com.miszmaniac.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

data class ViewBinder<LayoutBindingClass : ViewBinding, DataType>(
    val viewBindingInflater: (LayoutInflater, ViewGroup, Boolean) -> LayoutBindingClass,
    val bind: LayoutBindingClass.(data: DataType) -> Unit
)