import com.hilingual.buildlogic.DependencyManager
import org.gradle.api.Plugin
import org.gradle.api.Project

class SerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            DependencyManager.addSerializationDependencies(this)
        }
    }
}
