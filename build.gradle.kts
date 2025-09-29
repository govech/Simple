// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.detekt) apply false
}

// 配置全局的代码质量检查
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    
    detekt {
        config = files("$rootDir/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
        autoCorrect = true
    }
}
