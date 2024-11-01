plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.org.jetbrains.kotlinx.serialization)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.googleSecrets)
    alias(libs.plugins.googleKsp)
    alias(libs.plugins.firebase)
}

android {
    namespace = "com.sustainhive.ecoconnect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sustainhive.ecoconnect"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Haze
//    implementation(libs.haze)

    //reCaptcha
    implementation(libs.recaptcha)

    //Coil
    implementation(libs.coil)
    implementation(libs.coil.gif)
    implementation(libs.coil.network.okhttp)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.ktx)
    implementation(libs.firebase.auth.ktx)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //Mapbox
    implementation(libs.android)
    implementation(libs.maps.compose)

    //Play Services Location
    implementation(libs.play.services.location)

    //Message Bar
    implementation(libs.message.bar.compose)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Calendar
    implementation(libs.compose.calendar)

    //Vico
    implementation(libs.vico.compose)

    //Lottie
    implementation(libs.lottie.compose)

    //Accompanist
    implementation(libs.accompanist.permissions)

    //Data Store
    implementation(libs.androidx.datastore.preferences)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.common)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.coroutinesSupport)

    //Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.animation)

    //KotlinX Serialization Json
    implementation(libs.kotlinxSerialization)

    //Miscellaneous
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
}