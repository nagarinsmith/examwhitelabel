plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Constants.targetSdkVersion)
    buildToolsVersion(Constants.buildToolsVersion)
    defaultConfig {
        applicationId = "com.examwhitelabel"
        minSdkVersion(Constants.minSdkVersion)
        targetSdkVersion(Constants.targetSdkVersion)
        versionCode = Constants.versionCode
        versionName = Constants.versionName
    }
    dataBinding {
        isEnabled = true
    }
    androidExtensions {
        isExperimental = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    Dependencies.androidDependencies.forEach(::implementation)
    Dependencies.androidKapt.forEach(::kapt)
}
