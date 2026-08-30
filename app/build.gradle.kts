plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.phim"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.phim"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures{
        compose=true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material)
    implementation("androidx.activity:activity-compose:...")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:...")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:...")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:...")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}