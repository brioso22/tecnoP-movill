plugins {
    alias(libs.plugins.android.application) // Elimina esta línea si prefieres usar la siguiente línea directa
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")


}

android {
    namespace = "com.example.tecnof"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tecnof"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.recyclerview)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.5.1")) // Firebase BOM
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

// Google Play Services dependency
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    // Picasso dependency for image loading
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.airbnb.android:lottie:6.0.0")

    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.0")
    implementation ("com.google.firebase:firebase-firestore:24.0.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("androidx.cardview:cardview:1.0.0")



}
