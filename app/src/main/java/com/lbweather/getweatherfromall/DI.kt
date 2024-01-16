package com.lbweather.getweatherfromall


import org.koin.dsl.module

val dataModule = module {
    // repository
//    single<DefaultRepository> {
//        DefaultRepositoryImp(
//            get(),
//            get(),
//            get(),
//            get(),
//            get(),
//            get()
//        )
//    }
//
//    // use cases
//    factory { UseCaseGetData(get()) }
//    factory { UseCaseDataLocation(get()) }
//    factory { UseCaseCurrentLocation(get()) }
//    factory { UseCaseInternet(get()) }
//    factory { UseCaseLocationPermission(get()) }
//    factory { UseCaseSettings(get()) }
//
//    // use case`s components
//    single { ClientHTTPS() }
//    single<MyDataBase> {
//        Room.databaseBuilder(
//            get(),
//            MyDataBase::class.java,
//            "myLocationDB"
//        ).addMigrations(
//            MIGRATION_1_2
//        ).build()
//    }
//    single<MyDao> { get<MyDataBase>().locationDao() }
//    single { MyStorageLocation(get()) }
//    single { ConnectionManager(get()) }
//    single { LocationRequest(get()) }
//    single { MyStorageSettings(get()) }
//
//    //view-models
//    viewModel { ViewModelWeather(get()) }
//    viewModel { ViewModelLocation(get()) }
//    viewModel { ViewModelInternet(get()) }
}

