plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id ("kotlin-parcelize")
}

android {
    namespace = "ru.bogdan.patseev_diploma"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.bogdan.patseev_diploma"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")

    val lifecycle_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle_version")

    implementation ("com.google.dagger:dagger:2.51")
    kapt ("com.google.dagger:dagger-compiler:2.51")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    val camerax_version = "1.4.0-alpha04"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    implementation("com.airbnb.android:lottie:6.3.0")

    implementation ("com.google.android.gms:play-services-mlkit-barcode-scanning:16.1.0")
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")

    implementation ("com.auth0.android:jwtdecode:2.0.2")
}