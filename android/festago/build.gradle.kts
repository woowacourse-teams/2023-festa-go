// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val agpVersion = "8.0.2"
    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false

    val kotlinVersion = "1.8.20"
    kotlin("android") version kotlinVersion apply false
    kotlin("jvm") version kotlinVersion apply false
    kotlin("kapt") version kotlinVersion apply false

    id("org.jlleitschuh.gradle.ktlint") version "11.1.0" apply false

    id("com.google.gms.google-services") version "4.3.15" apply false

    id("com.google.firebase.crashlytics") version "2.9.7" apply false

    id("com.google.dagger.hilt.android") version "2.44" apply false

    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10" apply false
}
