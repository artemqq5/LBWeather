package com.lbweather.myapplication.network.requestAPI


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherInstance {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    // moshi builder
    @Provides
    @Singleton
    fun moshiConverter(): Moshi {
        return Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // retrofit builder with convertor moshi
    @Provides
    @Singleton
    fun retrofitInstance(moshi: Moshi): Retrofit  {
        return Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }


    // lazy create model request with interface
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): WeatherGetAPI {
        return retrofit.create(WeatherGetAPI::class.java)
    }
}