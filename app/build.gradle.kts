import java.util.Properties

plugins {
    alias(libs.plugins.android.application)

    id("com.google.gms.google-services")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")

    if (file.exists()) {
        file.inputStream().use {
            load(it)
        }
    }
}

val tmdbAccessToken =
    localProperties.getProperty(
        "TMDB_ACCESS_TOKEN",
        ""
    )

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

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "TMDB_ACCESS_TOKEN",
            "\"$tmdbAccessToken\""
        )
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(
        libs.androidx.lifecycle.viewmodel.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(libs.androidx.constraintlayout)

    implementation(
        libs.androidx.navigation.fragment.ktx
    )

    implementation(
        libs.androidx.navigation.ui.ktx
    )

    /*
     * Firebase
     */
    implementation(
        platform(
            "com.google.firebase:firebase-bom:34.18.0"
        )
    )

    implementation(
        "com.google.firebase:firebase-auth"
    )

    implementation(
        "com.google.firebase:firebase-firestore"
    )

    /*
     * TMDB
     */
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    /*
     * Movie posters
     */
    implementation(libs.glide)

    testImplementation(libs.junit)

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )
}