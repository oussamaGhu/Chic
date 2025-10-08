plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "tn.esprit.chiccercle"
    compileSdk = 34

    defaultConfig {
        applicationId = "tn.esprit.chiccercle"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
    }
}

dependencies {

    implementation("com.google.accompanist:accompanist-pager:0.36.0")

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.material:material:1.5.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("androidx.compose.ui:ui-test-android:1.7.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")

    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.5.0")

    // Retrofit and OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("org.mongodb:bson-kotlinx:5.2.0")
    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    //for camera
    implementation("androidx.camera:camera-core:1.4.0")
    implementation("androidx.camera:camera-lifecycle:1.4.0")
    implementation("androidx.camera:camera-view:1.4.0")
    implementation("androidx.camera:camera-camera2:1.4.0")
    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.4.0")

    // ViewModel support for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Coil for Image Loading
    implementation("io.coil-kt:coil-compose:2.1.0")

    // Navigation Component for Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.7.3")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Dependency Injection with Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")

    // Logging with Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Paging (Optional)
    implementation("androidx.paging:paging-compose:1.0.0-alpha19")


    // Google Play Services Auth
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    // Test Dependencies
    testImplementation("junit:junit:4.13.2")

    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
}