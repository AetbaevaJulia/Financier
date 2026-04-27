package com.example.financier.di

import android.app.Application
import com.example.financier.di.binds.AppBindsModule
import com.example.financier.di.viewModel.ViewModelModule
import com.example.financier.presenter.fragments.StartFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Component(
    modules = [AppModule::class]
)
@Singleton
abstract class AppComponent {

    abstract fun inject(fragment: StartFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
}

@Module(
    includes = [
        AppBindsModule::class,
        ViewModelModule::class
    ]
)
class AppModule