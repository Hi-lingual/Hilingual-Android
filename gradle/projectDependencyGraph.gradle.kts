import org.gradle.api.artifacts.ProjectDependency
import java.io.File

tasks.register("projectDependencyGraph") {
    group = "reporting"
    description = "Generates a simplified DOT graph of Android module dependencies."

    doLast {
        val dotDir = File(rootProject.projectDir, "graph")
        val dotFile = File(dotDir, "project.dot")
        dotDir.mkdirs()

        val androidProjects = subprojects.filter {
            it.plugins.hasPlugin("com.android.application") || it.plugins.hasPlugin("com.android.library")
        }

        val dependencies = mutableListOf<Triple<String, String, String?>>()

        dotFile.bufferedWriter().use { writer ->
            writer.appendLine("digraph {")
            writer.appendLine("  graph [label=\"${rootProject.name}\\n\", labelloc=t, fontsize=30, ranksep=1.4];")
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
            androidProjects.forEach { project ->
                project.configurations
                    .filter { it.name.contains("implementation", ignoreCase = true) }
                    .forEach { config ->
                        config.dependencies.withType(ProjectDependency::class.java).forEach { dep ->
                            val from = project.path
                            val to = dep.dependencyProject.path
                            if (androidProjects.map { it.path }.contains(to)) {
                                dependencies.add(Triple(from, to, "style=dotted"))
                            }
                        }
                    }
            }

            dependencies.forEach { (from, to, style) ->
                writer.append("  \"$from\" -> \"$to\"")
                if (style != null) writer.append(" [$style]")
                writer.appendLine()
            }

            writer.appendLine("}")
        }

        exec {
            commandLine("dot", "-Tpng", "-O", dotFile.name)
            workingDir = dotFile.parentFile
        }

        dotFile.delete()
        println("✅ Graph generated at: ${dotFile.absolutePath}.png")
    }
}
