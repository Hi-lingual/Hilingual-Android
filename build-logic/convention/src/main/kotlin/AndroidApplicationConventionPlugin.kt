import com.android.build.api.dsl.ApplicationExtension
import com.hilingual.buildlogic.DependencyManager
import com.hilingual.buildlogic.configureBuildTypes
import com.hilingual.buildlogic.configureKotlinAndroid
import com.hilingual.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("hilingual.hilt")
                apply("hilingual.serialization")
                apply("hilingual.android.compose")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
                configureKotlinAndroid(this)
                configureBuildTypes(this)
            }

            DependencyManager.addAndroidLibraryDependencies(this)
        }
    }
}
