import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    compileSdk = 34
    namespace = "com.lbweather.getweatherfromall"

    defaultConfig {
        applicationId = "com.lbweather.getweatherfromall"
        minSdk = 24
        targetSdk = 34
        versionCode = 6
        versionName = "2.1"
        viewBinding.enable = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val propertiesFile = project.rootProject.file("local.properties")
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use {
                properties.load(it)
            }
        }

        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
        buildConfigField(
            "String",
            "ID_ON_START_ACTIVITY_INTERSTITIAL",
            "\"${properties.getProperty("ID_ON_START_ACTIVITY_INTERSTITIAL")}\""
        )
        buildConfigField(
            "String",
            "ID_BOTTOM_SHEET_BANNER",
            "\"${properties.getProperty("ID_BOTTOM_SHEET_BANNER")}\""
        )
        buildConfigField(
            "String",
            "ID_FUTURE_WEATHER_BANNER",
            "\"${properties.getProperty("ID_FUTURE_WEATHER_BANNER")}\""
        )
        buildConfigField(
            "String",
            "ID_LOCATION_SEARCH_BANNER",
            "\"${properties.getProperty("ID_LOCATION_SEARCH_BANNER")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-perf")

    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("com.airbnb.android:lottie:6.3.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.facebook.android:facebook-android-sdk:16.3.0")

    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
