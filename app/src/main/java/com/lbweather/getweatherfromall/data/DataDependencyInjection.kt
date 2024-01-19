package com.lbweather.getweatherfromall.data

import androidx.room.Room
import com.lbweather.getweatherfromall.data.database.LocationDao
import com.lbweather.getweatherfromall.data.database.MyDataBase
import com.lbweather.getweatherfromall.data.database.MyDataBase.Companion.DATABASE_NAME
import com.lbweather.getweatherfromall.data.storage.StoragePreference
import com.lbweather.getweatherfromall.data.storage.StoragePreferenceImp
import com.lbweather.getweatherfromall.domain.repository.LocationRepository
import com.lbweather.getweatherfromall.domain.repository.PreferenceRepository
import com.lbweather.getweatherfromall.domain.repository.WeatherRepository
import org.koin.dsl.module

val dataDI = module {

    single<LocationDao> {
        Room.databaseBuilder(
            get(),
            MyDataBase::class.java,
            DATABASE_NAME
        ).addMigrations(
//            MIGRATION_1_2
        ).createFromAsset("$DATABASE_NAME.db").build().getLocationDao()
    }

    single<WeatherRepository> {
        WeatherRepositoryImp(apiWeatherUseCase = get())
    }

    single<LocationRepository> {
        LocationRepositoryImp(dao = get(), apiLocationUseCase = get())
    }

    single<StoragePreference> {
        StoragePreferenceImp(context = get())
    }

    single<PreferenceRepository> {
        PreferenceRepositoryImp(storagePreference = get())
    }

}