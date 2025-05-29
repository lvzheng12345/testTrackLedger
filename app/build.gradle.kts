plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ncusoft.myapplication7"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ncusoft.myapplication7"
        minSdk = 34
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
}

configurations.all {
    resolutionStrategy {
        force("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.1")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // 图表库
    implementation(libs.mpandroidchart)

    // 日历控件，排除 support 库
    implementation(libs.calendarview) {
        exclude(group = "com.android.support")
    }
}