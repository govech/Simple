plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.framework.android"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.framework.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // 多语言支持配置
        resourceConfigurations.addAll(listOf("en", "zh-rCN"))
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            
            buildConfigField("String", "BASE_URL", "\"https://api-dev.example.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }
        
        create("staging") {
            initWith(getByName("debug"))
            isMinifyEnabled = true
            isDebuggable = false
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            
            buildConfigField("String", "BASE_URL", "\"https://api-staging.example.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        
        release {
            isMinifyEnabled = true
            isDebuggable = false
            
            buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
            
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            
            signingConfig = signingConfigs.getByName("debug") // 在实际项目中应该配置正式的签名
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // 测试配置
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.fragment.ktx)

    // Compose BOM - 管理所有 Compose 库的版本
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    
    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt 依赖注入
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // 网络请求
    implementation(libs.bundles.network)

    // 数据库
    implementation(libs.bundles.room)
    implementation(libs.datastore.preferences)
    kapt(libs.room.compiler)

    // 协程
    implementation(libs.bundles.coroutines)

    // 图片加载
    implementation(libs.coil.compose)

    // Material Design
    implementation(libs.material)

    // 单元测试
    testImplementation(libs.bundles.testing)
    
    // UI 测试
    androidTestImplementation(libs.bundles.android.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    
    // Debug 工具
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// Kapt 配置
kapt {
    correctErrorTypes = true
}
