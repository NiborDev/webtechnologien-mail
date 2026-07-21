import com.github.gradle.node.npm.task.NpmTask
import org.gradle.api.tasks.Copy

plugins {
  id("com.github.node-gradle.node") version "7.1.0"
}

apply(plugin = "idea")

node {
  download = true
  version = "24.12.0"
  npmVersion = "11.6.2"
}

tasks.register<NpmTask>("build") {
  group = "build"
  dependsOn("npmSetup", "npmInstall")
  args.set(listOf("run", "build"))
  workingDir.set(project.rootDir.resolve("mail_client"))
}

tasks.register<Copy>("installDist") {
  group = "distribution"
  dependsOn("build")
  from(project.layout.projectDirectory.dir("dist/mail_client"))
  into(project.layout.buildDirectory.dir("install"))
}

// clean task
tasks.register<Delete>("clean") {
  val modulePath = project.rootDir.resolve("mail_client")

  delete(modulePath.resolve(".angular"))
  delete(modulePath.resolve(".gradle"))
  delete(modulePath.resolve("build"))
  delete(modulePath.resolve("dist"))
  delete(modulePath.resolve("node_modules"))
}
