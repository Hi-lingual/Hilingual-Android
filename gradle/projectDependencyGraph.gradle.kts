import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.artifacts.component.ProjectComponentSelector
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

abstract class ProjectDependencyGraphTask : org.gradle.api.DefaultTask() {

    @get:Inject
    protected abstract val execOperations: ExecOperations

    @org.gradle.api.tasks.TaskAction
    fun run() {
        val dotDir = File(project.rootDir, "graph")
        val dotFile = File(dotDir, "project.dot")
        dotDir.mkdirs()

        val androidProjects = project.subprojects.filter {
            it.plugins.hasPlugin("com.android.application") || it.plugins.hasPlugin("com.android.library")
        }

        val dependencies = mutableListOf<Triple<String, String, String?>>()

        dotFile.bufferedWriter().use { writer ->
            writer.appendLine("digraph {")
            writer.appendLine("  graph [label=\"${project.rootProject.name}\\n\", labelloc=t, fontsize=30, ranksep=1.4];")
            writer.appendLine("  node [style=filled, shape=ellipse];")
            writer.appendLine("  rankdir=TB;")

            writer.appendLine("\n  # Modules")
            androidProjects.forEach { project ->
                val path = project.path
                val color = when {
                    path.startsWith(":core") -> "#ffd2b3"
                    path.startsWith(":data") -> "#baffc9"
                    path.startsWith(":presentation") -> "#add8e6"
                    path == ":app" -> "#c9baff"
                    else -> "#eeeeee"
                }
                writer.appendLine("  \"$path\" [fillcolor=\"$color\"];")
            }

            writer.appendLine("\n  # Dependencies")
            androidProjects.forEach { currentProject ->
                currentProject.configurations
                    .filter { it.isCanBeResolved && it.name.contains("implementation", ignoreCase = true) }
                    .forEach { config ->
                        config.incoming.resolutionResult.root.dependencies
                            .filterIsInstance<ResolvedDependencyResult>()
                            .forEach { resolvedDependency ->
                                val requestedComponent = resolvedDependency.requested
                                if (requestedComponent is ProjectComponentSelector) {
                                    val from = currentProject.path
                                    val to = requestedComponent.projectPath
                                    if (androidProjects.any { it.path == to }) {
                                        dependencies.add(Triple(from, to, "style=dotted"))
                                    }
                                }
                            }
                    }
            }

            dependencies.distinct().forEach { (from, to, style) ->
                writer.append("  \"$from\" -> \"$to\"")
                if (style != null) writer.append(" [$style]")
                writer.appendLine()
            }

            writer.appendLine("}")
        }

        execOperations.exec {
            commandLine("dot", "-Tpng", "-O", dotFile.name)
            workingDir(dotFile.parentFile)
        }

        dotFile.delete()
        println("✅ Graph generated at: ${dotFile.absolutePath}.png")
    }
}

tasks.register<ProjectDependencyGraphTask>("projectDependencyGraph") {
    group = "reporting"
    description = "Generates a simplified DOT graph of Android module dependencies."
}
