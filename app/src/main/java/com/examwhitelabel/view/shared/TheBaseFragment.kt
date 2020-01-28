package com.examwhitelabel.view.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.examwhitelabel.BR
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class TheBaseFragment<BINDING : ViewDataBinding, VIEW_MODEL : ViewModel>(
    @LayoutRes private val layoutResource: Int,
    viewModelClass: KClass<VIEW_MODEL>
) : Fragment() {

    protected var binding: BINDING by autoCleared()
        private set

    protected val viewModel: VIEW_MODEL by viewModel(viewModelClass) { parametersOf(*viewModelParams) }

    open val viewModelParams: Array<Any?> = arrayOf()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<BINDING>(inflater, layoutResource, null, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.setVariable(BR.viewModel, viewModel)
            binding = it
        }.root
}
