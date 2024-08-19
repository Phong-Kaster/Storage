plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.storage"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.storage"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        // Configuration for the 'debug' build type
        /*getByName("debug") {
            storeFile = file("key/jetpack.jks") // Path to the keystore file
            storePassword = "123465"                  // Password for the keystore
            keyAlias = "jetpack"                        // Alias for the key
            keyPassword = "123456"                    // Password for the key
        }*/

        // Creates a new configuration for the 'release' build type
        create("release") {
            storeFile = file("key/keystore.jks") // Path to the keystore file
            storePassword = "jetpack123456"           // Password for the keystore
            keyAlias = "jetpack"                     // Alias for the key
            keyPassword = "jetpack123456"            // Password for the key
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dependency injection with Hilt - https://developer.android.com/training/dependency-injection/hilt-android#setup
    // Hilt Android Processor - https://mvnrepository.com/artifact/com.google.dagger/hilt-android-compiler
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")

    // Preferences DataStore - https://developer.android.com/topic/libraries/architecture/datastore#preferences-datastore-dependencies
    implementation("androidx.datastore:datastore-preferences:1.1.1")


}

// Dependency injection with Hilt - Allow references to generated code
kapt {
    correctErrorTypes = true
}