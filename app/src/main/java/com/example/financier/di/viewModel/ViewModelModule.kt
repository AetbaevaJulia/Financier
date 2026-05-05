package com.example.financier.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financier.presenter.viewModels.MainViewModel
import com.example.financier.presenter.viewModels.OperationsInCategoryViewModel
import com.example.financier.presenter.viewModels.RegularOperationsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(
        viewModel: MainViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OperationsInCategoryViewModel::class)
    fun bindOperationsInCategoryViewModel(
        viewModel: OperationsInCategoryViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegularOperationsViewModel::class)
    fun bindRegularOperationsViewModel(
        viewModel: RegularOperationsViewModel
    ): ViewModel
}