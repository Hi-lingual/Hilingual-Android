import com.hilingual.buildlogic.DependencyManager
import com.hilingual.buildlogic.androidExtension
import com.hilingual.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            configureAndroidCompose(androidExtension)
            DependencyManager.addComposeDependencies(this)
        }
    }
}
