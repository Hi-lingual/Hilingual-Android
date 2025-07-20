import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import java.io.File
import java.util.Queue
import java.util.LinkedList

tasks.register("projectDependencyGraph") {
    group = "reporting"
    description = "Generates a DOT graph of project module dependencies."

    doLast {
        val dotFile = File(rootProject.layout.buildDirectory.get().asFile, "reports/dependency-graph/project.dot")
        dotFile.parentFile.mkdirs()
        dotFile.delete()

        dotFile.bufferedWriter().use { writer ->
            writer.appendLine("digraph {")
            writer.appendLine("  graph [label=\"${rootProject.name}\\n \",labelloc=t,fontsize=30,ranksep=1.4];")
            writer.appendLine("  node [style=filled, fillcolor=\"#bbbbbb\"];")
            writer.appendLine("  rankdir=TB;")

            val projects = mutableSetOf<Project>()
            val dependencies = mutableMapOf<Pair<Project, Project>, MutableList<String>>()
            val rootProjects = mutableSetOf<Project>()

            val queue: Queue<Project> = LinkedList()
            queue.add(rootProject)

            while (queue.isNotEmpty()) {
                val project = queue.poll()
                rootProjects.add(project)
                project.childProjects.values.forEach { queue.add(it) }
            }

            queue.add(rootProject)
            while (queue.isNotEmpty()) {
                val currentProject = queue.poll()
                currentProject.childProjects.values.forEach { queue.add(it) }

                currentProject.configurations.filter { !it.name.lowercase().contains("test") }.forEach { config ->
                    config.dependencies
                        .withType(ProjectDependency::class.java)
                        .forEach { projectDependency ->
                            val targetProject = projectDependency.dependencyProject
                            projects.add(currentProject)
                            projects.add(targetProject)
                            rootProjects.remove(targetProject) // Remove if it's a dependency of another project

                            val graphKey = Pair(currentProject, targetProject)
                            val traits = dependencies.computeIfAbsent(graphKey) { mutableListOf() }

                            if (config.name.lowercase().endsWith("implementation")) {
                                traits.add("style=dotted")
                            }
                        }
                }
            }

            val sortedProjects = projects.sortedBy { it.path }

            writer.appendLine("\n  # Projects\n")
            for (project in sortedProjects) {
                val traits = mutableListOf<String>()

                if (rootProjects.contains(project)) {
                    traits.add("shape=box")
                }

                // Check for specific plugins to determine fillcolor
                // Note: This part needs to be adapted based on actual plugin IDs used in Hilingual
                // For now, using generic checks. You might need to refine these based on your build.gradle.kts
                if (project.plugins.hasPlugin("com.android.application")) {
                    traits.add("fillcolor=\"#baffc9\"") // Light green for application
                } else if (project.plugins.hasPlugin("com.android.library")) {
                    traits.add("fillcolor=\"#add8e6\"") // Light blue for Android libraries
                } else if (project.plugins.hasPlugin("org.jetbrains.kotlin.jvm") || project.plugins.hasPlugin("java-library") || project.plugins.hasPlugin("java")) {
                    traits.add("fillcolor=\"#ffb3ba\"") // Light red/pink for JVM libraries
                } else if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                    traits.add("fillcolor=\"#ffd2b3\"") // Orange for multiplatform (if any)
                } else if (project.plugins.hasPlugin("org.jetbrains.kotlin.js")) {
                    traits.add("fillcolor=\"#ffffba\"") // Yellow for JS (if any)
                } else if (project.plugins.hasPlugin("com.android.dynamic-feature")) {
                    traits.add("fillcolor=\"#c9baff\"") // Purple for dynamic features (if any)
                } else {
                    traits.add("fillcolor=\"#eeeeee\"") // Default light grey
                }

                writer.appendLine("  \"${project.path}\" [${traits.joinToString(", ")}];")
            }

            writer.appendLine("\n  {rank = same;")
            for (project in sortedProjects) {
                if (rootProjects.contains(project)) {
                    writer.appendLine(" \"${project.path}\";")
                }
            }
            writer.appendLine("}")

            writer.appendLine("\n  # Dependencies\n")
            dependencies.forEach { (key, traits) ->
                writer.append("  \"${key.first.path}\" -> \"${key.second.path}\"")
                if (traits.isNotEmpty()) {
                    writer.append(" [${traits.joinToString(", ")}]")
                }
                writer.appendLine()
            }

            writer.appendLine("}")
        }

        exec {
            commandLine("dot", "-Tpng", "-O", "project.dot")
            workingDir = dotFile.parentFile
        }
        dotFile.delete() // Delete the .dot file after generating png

        println("Project module dependency graph created at ${dotFile.absolutePath}.png")
    }
}