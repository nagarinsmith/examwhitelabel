package com.examwhitelabel.view.shared

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("app:isVisible")
fun View.isVisible(state: Boolean?) {
    isVisible = state ?: return
}
