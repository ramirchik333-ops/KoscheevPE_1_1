plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.unitconverter"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.unitconverter"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../signing/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            storeFile = file("../signing/release.keystore")
            storePassword = "converter2024"
            keyAlias = "converter"
            keyPassword = "converter2024"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            optimization {
                enable = true
                packageScope = setOf("com.example.unitconverter.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Lint запускается отдельной задачей: релизная сборка не должна от него зависеть.
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

// Приложение использует только платформенные API Android: сторонних библиотек нет.
dependencies {
    testImplementation(libs.junit)
}
