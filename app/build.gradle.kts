plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("androidx.navigation.safeargs.kotlin")
    id("androidx.room")
    id("com.google.devtools.ksp")
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(type = "String", name = "HH_ACCESS_TOKEN", value = "\"${developProperties.hhAccessToken}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)
    implementation(libs.androidX.fragment)

    // UI Layer Libraries
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)

    // Network
    implementation(libs.network.retrofit)
    implementation(libs.network.converterGson)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)

    // Dependency Injection
    implementation(libs.koin.android)

    // Navigation
    implementation(libs.navigation.fragmentKtx)
    implementation(libs.navigation.uiKtx)

    // Database
    implementation(libs.db.roomRuntime)
    annotationProcessor(libs.db.roomCompiler)
    ksp(libs.db.roomCompiler)
    implementation(libs.db.roomKtx)

    // Unit Tests
    testImplementation(libs.unitTests.junit)

    // UI Tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
}
