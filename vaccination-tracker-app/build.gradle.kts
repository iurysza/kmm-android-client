plugins {
  id("com.android.application")
  id("kotlin-android")
}

val composeVersion: String by project

android {
  compileSdk = 30

  defaultConfig {
    applicationId = "com.github.iurysza.vacctrackerapp"
    minSdk = 29
    targetSdk = 30
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
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
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = composeVersion
  }
}


dependencies {

  implementation("com.github.iurysza:vaccination-tracker:1.0.17")
  implementation("androidx.core:core-ktx:1.6.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
  implementation("androidx.activity:activity-compose:${composeVersion}")
  implementation("androidx.compose.ui:ui:${composeVersion}")
  implementation("androidx.compose.material:material:${composeVersion}")
  implementation("androidx.compose.ui:ui-tooling:${composeVersion}")
  implementation("androidx.activity:activity-compose:1.3.0")
  implementation("com.google.android.material:material:1.3.0")
  
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:${composeVersion}")
}