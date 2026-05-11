package com.example.financier.di

import android.app.Application
import com.example.financier.di.binds.AppBindsModule
import com.example.financier.di.binds.NetworkModule
import com.example.financier.di.viewModel.ViewModelModule
import com.example.financier.presenter.fragments.MainFragment
import com.example.financier.presenter.fragments.OperationsInCategoryFragment
import com.example.financier.presenter.fragments.RegularOperationsFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Component(
    modules = [AppModule::class]
)
@Singleton
abstract class AppComponent {

    abstract fun inject(fragment: MainFragment)
    abstract fun inject(fragment: OperationsInCategoryFragment)
    abstract fun inject(fragment: RegularOperationsFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
}

@Module(
    includes = [
        NetworkModule::class,
        AppBindsModule::class,
        ViewModelModule::class
    ]
)
class AppModule