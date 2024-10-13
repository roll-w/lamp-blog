/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("lamp-project")
}

tasks.register<Tar>("package") {
    dependsOn(":lamp-blog-web:assemble")

    group = "build"
    description = "Creates a distribution package for the project"

    val baseDir = "/lamp-blog-${version}"
    into(baseDir) {
        from("${project.rootDir}/distribution/README")
        from("${project.rootDir}/distribution/NOTICE")
        from("${project.rootDir}/LICENSE")
    }
    into("${baseDir}/lib") {
        from("${project(":lamp-blog-web").projectDir}/build/libs/lamp-blog-web-${version}.jar") {
            rename("lamp-blog-web-${version}.jar", "lamp-blog.jar")
        }
    }
    into("${baseDir}/bin") {
        from("${project.projectDir}/scripts/lamp.sh") {
            rename("lamp.sh", "lamp")
            filePermissions {
                unix(755)
            }
        }
    }

    into("${baseDir}/conf") {
        from("${project(":lamp-blog-web").projectDir}/src/main/resources/lamp.conf")
    }

    archiveFileName = "lamp-blog-${version}.tar.gz"
    destinationDirectory = file("${project.projectDir}/build/dist")
    compression = Compression.GZIP
}

tasks.register("build-image") {
    dependsOn(":lamp-blog-web:assemble")
}

tasks.register<Exec>("buildFrontend") {
    group = "build"
    description = "Build frontend of this project"

    workingDir = file("${project.projectDir}/lamp-blog-frontend")

    val npm = when {
        Os.isFamily(Os.FAMILY_WINDOWS) -> "npm.cmd"
        else -> "npm"
    }
    commandLine = listOf(npm, "run", "build")
    standardOutput = System.out
    outputs.upToDateWhen { false }
}

tasks.register<Tar>("packageFrontend") {
    group = "build"
    description = "Packs frontend of this project"

    dependsOn("buildFrontend")

    into("lamp-blog-frontend") {
       from("${project.projectDir}/lamp-blog-frontend/dist")
    }
    // TODO: may move to package task

    archiveFileName.set("lamp-blog-frontend-${version}.tar.gz")
    destinationDirectory.set(file("${project.projectDir}/build/dist"))
    compression = Compression.GZIP
}

