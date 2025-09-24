import com.hilingual.buildlogic.DependencyManager
import org.gradle.api.Plugin
import org.gradle.api.Project

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.dagger.hilt.android")
            // KSP는 Hilt와 거의 항상 함께 사용되므로, 같이 적용해주는 것이 편리합니다.
            pluginManager.apply("com.google.devtools.ksp")

            DependencyManager.addHiltDependencies(this)
        }
    }
}
