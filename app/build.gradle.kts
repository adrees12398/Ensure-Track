plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.ensuretrackapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ensuretrackapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    viewBinding{
        enable = true
    }
}

dependencies {
    // Core Android + Kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // ── Firebase ────────────────────────────────────────────────
    // Use the BOM → all Firebase libraries get the same version automatically
    implementation(platform("com.google.firebase:firebase-bom:34.1.0"))  // or latest 34.x

    // Correct way: NO -ktx suffix anymore (KTX APIs are now in the main modules)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")       // ← this was your problem
    implementation("com.google.firebase:firebase-analytics")

    // Other dependencies
    implementation("com.github.bumptech.glide:glide:5.0.5")
    implementation("com.facebook.android:facebook-android-sdk:latest.release")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation(libs.google.material)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
