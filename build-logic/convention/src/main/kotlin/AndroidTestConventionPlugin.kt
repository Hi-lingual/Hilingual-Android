import com.android.build.api.dsl.TestExtension
import com.hilingual.buildlogic.DependencyManager
import com.hilingual.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
            }
            DependencyManager.addAndroidLibraryDependencies(this)
        }
    }
}
