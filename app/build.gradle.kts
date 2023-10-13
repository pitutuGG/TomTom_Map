plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.tomtom"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tomtom"
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
            buildConfigField("String", "TOMTOM_API_KEY", "\"4qYsSQeBWlI3vmtpw6YGPDR3UaoaMadT\"")
        }
        debug {
            buildConfigField("String", "TOMTOM_API_KEY", "\"4qYsSQeBWlI3vmtpw6YGPDR3UaoaMadT\"")
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
    packaging {
        resources {
            jniLibs.pickFirsts.add("lib/**/libc++_shared.so")
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("com.tomtom.sdk.maps:map-display:0.32.6")
    implementation("com.tomtom.sdk.location:provider-proxy:0.32.6")
    implementation("com.tomtom.sdk.location:provider-gms:0.32.6")
    implementation("com.tomtom.sdk.location:provider-api:0.32.6")
    implementation("com.tomtom.sdk.location:provider-android:0.32.6")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.compose.runtime:runtime:1.0.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("com.google.android.material:material:1.3.0-alpha01")
}