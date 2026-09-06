import java.util.Properties
import java.util.Base64
import java.nio.charset.StandardCharsets

plugins {
    alias(libs.plugins.android.application)
}

val properties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream())
}
val baseUrlValue = properties.getProperty("base_url") ?: "103.103.20.61"

// Fungsi enkripsi sederhana untuk menyamarkan IP
fun encrypt(input: String): String {
    val key = "CBT_SECRET_KEY"
    val xored = input.mapIndexed { index, char ->
        (char.code xor key[index % key.length].code).toChar()
    }.joinToString("")
    return Base64.getEncoder().encodeToString(xored.toByteArray(StandardCharsets.UTF_8))
}

val encryptedBaseUrl = encrypt(baseUrlValue)

android {
    namespace = "com.smkth46.cbt_th_46_v3"
    compileSdk = 35 // Menggunakan versi SDK yang lebih stabil jika perlu, atau tetap 37

    defaultConfig {
        applicationId = "com.smkth46.cbt_th_46_v3"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "ENCRYPTED_URL", "\"$encryptedBaseUrl\"")
    }

    buildFeatures {
        buildConfig = true
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
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}