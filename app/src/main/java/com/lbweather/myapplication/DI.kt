package com.lbweather.myapplication

import com.lbweather.myapplication.data.DefaultRepositoryImp
import com.lbweather.myapplication.domain.UseCaseGetData
import com.lbweather.myapplication.domain.network.ClientHTTPS
import com.lbweather.myapplication.domain.repository.DefaultRepository
import com.lbweather.myapplication.presentation.viewmodel.ViewModelWeather
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    // repository
    single<DefaultRepository> {
        DefaultRepositoryImp(get())
    }

    // use cases
    factory {
        UseCaseGetData(get())
    }

    // use case`s components
    single {
        ClientHTTPS()
    }

    //view-models
    viewModel {
        ViewModelWeather(get())
    }
}

//val domainModule = module {
//
//}
//
//val domainPresentation = module {
//
//}