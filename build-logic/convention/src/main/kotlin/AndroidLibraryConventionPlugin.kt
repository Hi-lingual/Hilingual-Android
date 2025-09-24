import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.hilingual.buildlogic.DependencyManager
import com.hilingual.buildlogic.configureBuildTypes
import com.hilingual.buildlogic.configureKotlinAndroid
import com.hilingual.buildlogic.disableUnnecessaryAndroidTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")
            pluginManager.apply("org.jlleitschuh.gradle.ktlint")

            extensions.configure<LibraryExtension> { 
                configureKotlinAndroid(this)
                configureBuildTypes(this)
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }
            DependencyManager.addAndroidLibraryDependencies(this)
        }
    }
}
