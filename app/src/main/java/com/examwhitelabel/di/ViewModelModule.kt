package com.examwhitelabel.di

import com.examwhitelabel.data.TheItem
import com.examwhitelabel.view.addOrEdit.TheAddOrEditViewModel
import com.examwhitelabel.view.detail.TheDetailViewModel
import com.examwhitelabel.view.list.TheListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val module = module {
        viewModel { TheListViewModel(get(), get(), get(), get(), get()) }
        viewModel { (item: TheItem) -> TheDetailViewModel(item, get(), get()) }
        viewModel { (item: TheItem?) -> TheAddOrEditViewModel(item, get(), get()) }
    }
}
