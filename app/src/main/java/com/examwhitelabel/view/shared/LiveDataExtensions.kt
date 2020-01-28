package com.examwhitelabel.view.shared

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    this.observe(lifecycleOwner, Observer { data ->
        data?.let { observer(it) }
    })
}
