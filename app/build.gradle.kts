plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.dam.simonmedijo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dam.simonmedijo"
        minSdk = 36
        targetSdk = 36
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
        compose = true
    }


}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation("androidx.test:core:1.5.0")


    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // ✅ JUnit 4
    //Web de Junit 4 : https://junit.org/junit4/
    testImplementation("junit:junit:4.13.2")

    // Mockito (JUnit 4)
    // Web de Mockito : https://site.mockito.org/
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito:mockito-inline:5.2.0") // para mocks de clases finales
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("io.mockk:mockk:1.13.7")

    // Robolectric
    // Web de Robolectric : https://developer.android.com/training/testing/local-tests/robolectric?hl=es-419
    testImplementation("org.robolectric:robolectric:4.11.1")

}
