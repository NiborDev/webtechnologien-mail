tasks.register("installDist") {
    description = "Builds notes web client and notes api for distribution"
    group = "distribution"
    dependsOn(":MailClient:installDist")
    dependsOn(":mail_backend:installDist")
    doLast {
        copy {
            from(project(":mail_backend").layout.buildDirectory.dir("install"))
            into(project.rootDir.resolve("build/install"))
        }
        copy {
            from(project(":MailClient").layout.buildDirectory.dir("install/browser"))
            into(project.rootDir.resolve("build/install/MailClient"))
        }
    }
}
tasks.register("clean") {
    description = "Cleans generated artifacts"
    group = "build"
    dependsOn(":MailClient:clean")
    dependsOn(":mail_backend:clean")
    delete(project.rootDir.resolve("build"))
}