import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.festago.festago.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "kakao_redirection_scheme", getSecretKey("kakao_redirection_scheme"))
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    dataBinding {
        enable = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))

    // android
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    // recyclerview
    implementation("androidx.recyclerview:recyclerview:1.3.1-rc01")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // junit4
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test:runner:1.5.2")

    // assertJ
    testImplementation("org.assertj:assertj-core:3.22.0")

    // android-test
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // mock
    testImplementation("io.mockk:mockk-android:1.13.5")

    // espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.6.0")

    // zxing
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // firebase
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")

    // swiperefreshlayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // kakao login
    implementation("com.kakao.sdk:v2-user:2.12.0")

    // turbine
    testImplementation("app.cash.turbine:turbine:1.0.0")

    // inApp Update
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // splash
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")
}

fun getSecretKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}