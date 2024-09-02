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

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "lamp-blog"
include(":lamp-blog-web")
include(":lamp-blog-email")
include(":lamp-blog-file")
include(":lamp-blog-file-awss3")
include(":lamp-blog-user")
include(":lamp-blog-user-api")
include(":lamp-blog-user-service")
include(":lamp-blog-common")
include(":lamp-blog-file-api")
include(":lamp-blog-content")
include(":lamp-blog-content:content-api")
include(":lamp-blog-content:content-service")

project(":lamp-blog-content:content-api").projectDir = file("lamp-blog-content/lamp-blog-content-api")
project(":lamp-blog-content:content-service").projectDir = file("lamp-blog-content/lamp-blog-content-service")
project(":lamp-blog-user-api").projectDir = file("lamp-blog-user/lamp-blog-user-api")
project(":lamp-blog-user-service").projectDir = file("lamp-blog-user/lamp-blog-user-service")
project(":lamp-blog-file-api").projectDir = file("lamp-blog-file/lamp-blog-file-api")
project(":lamp-blog-file-awss3").projectDir = file("lamp-blog-file/lamp-blog-file-awss3")
