package com.lbweather.myapplication

import androidx.room.Room
import com.lbweather.myapplication.data.DefaultRepositoryImp
import com.lbweather.myapplication.domain.UseCaseCurrentLocation
import com.lbweather.myapplication.domain.UseCaseDataLocation
import com.lbweather.myapplication.domain.UseCaseGetData
import com.lbweather.myapplication.domain.network.ClientHTTPS
import com.lbweather.myapplication.domain.repository.DefaultRepository
import com.lbweather.myapplication.data.database.MyDao
import com.lbweather.myapplication.data.database.MyDataBase
import com.lbweather.myapplication.data.database.MyDataBase.Companion.MIGRATION_1_2
import com.lbweather.myapplication.data.datastore.MyStorage
import com.lbweather.myapplication.presentation.viewmodel.ViewModelLocation
import com.lbweather.myapplication.presentation.viewmodel.ViewModelWeather
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    // repository
    single<DefaultRepository> {
        DefaultRepositoryImp(get(), get(), get())
    }

    // use cases
    factory {
        UseCaseGetData(get())
    }
    factory {
        UseCaseDataLocation(get())
    }
    factory {
        UseCaseCurrentLocation(get())
    }

    // use case`s components
    single {
        ClientHTTPS()
    }
    single<MyDataBase> {
        Room.databaseBuilder(
            get(),
            MyDataBase::class.java,
            "myLocationDB"
        ).addMigrations(
            MIGRATION_1_2
        ).build()
    }
    single<MyDao> {
        get<MyDataBase>().locationDao()
    }
    single {
        MyStorage(get())
    }

    //view-models
    viewModel {
        ViewModelWeather(get())
    }
    viewModel {
        ViewModelLocation(get())
    }
}

//val domainModule = module {
//
//}
//
//val domainPresentation = module {
//
//}