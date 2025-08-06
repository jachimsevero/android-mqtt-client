import com.google.protobuf.gradle.id

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.dagger.hilt.android)
  alias(libs.plugins.kotlin.plugin.serialization)
  alias(libs.plugins.google.protobuf)
  alias(libs.plugins.devtools.ksp)
}

android {
  namespace = "com.jachimsevero.mqttclient"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.jachimsevero.mqttclient"
    minSdk = 27
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
  packagingOptions {
    resources {
      excludes += listOf("META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties")
    }
  }
  kotlin {
    jvmToolchain(21)
  }
  buildFeatures {
    compose = true
  }
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:3.22.4"
  }

  generateProtoTasks {
    all().forEach {
      it.builtins {
        id("java") {
          option("lite")
        }

        id("kotlin") {
          option("lite")
        }
      }
    }
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)

  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.kotlinx.serialization.json)

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)

  implementation(libs.androidx.datastore.core)
  implementation(libs.protobuf.javalite)
  implementation(libs.protobuf.kotlin.lite)

  implementation(libs.hivemq.mqtt.client)
  implementation(platform(libs.hivemq.mqtt.client.websocket))
  implementation(platform(libs.hivemq.mqtt.client.proxy))
  implementation(platform(libs.hivemq.mqtt.client.epoll))
  implementation(libs.hivemq.mqtt.client.reactor)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}