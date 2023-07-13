plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "1.8.22"
}

android {
    namespace = "com.festago.festago"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.festago.festago"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    dataBinding {
        enable = true
    }
}

dependencies {
    // android
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // recyclerview
    implementation("androidx.recyclerview:recyclerview:1.3.1-rc01")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // kotlin-serialization
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // junit5
    testImplementation("junit:junit:5.9.3")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.2")
    testImplementation("org.assertj", "assertj-core", "3.22.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // android-test
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // mock
    testImplementation("io.mockk:mockk-android:1.13.5")

    // espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.1")

    // viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.6.0")
}
