package com.jun.data

import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @BindingAdapter("text")
    @JvmStatic
    fun setText(view : TextView, text:String? ){
        view.text=text
    }
}