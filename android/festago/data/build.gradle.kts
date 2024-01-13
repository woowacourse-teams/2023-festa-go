import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.festago.festago.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        buildConfigField("String", "BASE_URL", getSecretKey("base_url"))
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

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin.jvmToolchain(17)

dependencies {
    implementation(project(":domain"))

    // hilt
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    // okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // okhttp3-mockwebserver
    implementation("com.squareup.okhttp3:mockwebserver:4.11.0")

    // kotlin-serialization
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Encrypted SharedPreference
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

    // kakao login
    implementation("com.kakao.sdk:v2-user:2.12.0")

    // junit4
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test:runner:1.5.2")

    // assertJ
    testImplementation("org.assertj:assertj-core:3.22.0")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}

fun getSecretKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}
