import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import java.io.File
import java.util.Queue
import java.util.LinkedList

tasks.register("projectDependencyGraph") {
    group = "reporting"
    description = "Generates a DOT graph of project module dependencies."

    doLast {
        val dotDir = File(rootProject.projectDir, "graph")
        val dotFile = File(dotDir, "project.dot")
        dotDir.mkdirs()

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
                            rootProjects.remove(targetProject)

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

                if (project.plugins.hasPlugin("com.android.application")) {
                    traits.add("fillcolor=\"#baffc9\"")
                } else if (project.plugins.hasPlugin("com.android.library")) {
                    traits.add("fillcolor=\"#add8e6\"")
                } else if (project.plugins.hasPlugin("org.jetbrains.kotlin.jvm") || project.plugins.hasPlugin("java-library") || project.plugins.hasPlugin("java")) {
                    traits.add("fillcolor=\"#ffb3ba\"")
                } else if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                    traits.add("fillcolor=\"#ffd2b3\"")
                } else if (project.plugins.hasPlugin("org.jetbrains.kotlin.js")) {
                    traits.add("fillcolor=\"#ffffba\"")
                } else if (project.plugins.hasPlugin("com.android.dynamic-feature")) {
                    traits.add("fillcolor=\"#c9baff\"")
                } else {
                    traits.add("fillcolor=\"#eeeeee\"")
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
            commandLine("dot", "-Tpng", "-O", dotFile.name)
            workingDir = dotFile.parentFile
        }

        dotFile.delete()

        println("Project module dependency graph created at ${dotFile.absolutePath}.png")
    }
}
