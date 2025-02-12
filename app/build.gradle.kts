plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.whtsappstatussaver"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.whtsappstatussaver"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.3.1")

   // implementation ("com.github.devlight.navigationtabstrip:navigationtabstrip:1.0.4")

   // implementation ("com.android.support:support-v4:23.1.1")
   // implementation ("com.github.hackware1993:MagicIndicator:1.6.0")
   // implementation ("com.github.hackware1993:MagicIndicator:1.7.0")

    //implementation("com.ogaclejapan.smarttablayout:library:1.7.0@aar")
    //implementation("com.ogaclejapan.smarttablayout:utils-v4:1.7.0@aar")
    //implementation("com.ogaclejapan.smarttablayout:utils-v13:1.7.0@aar")
}