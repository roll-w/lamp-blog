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
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "lamp-blog"

includeBuild("build-logic")

include(":lamp-blog-web")
include(":lamp-blog-email")
include(":lamp-blog-file")
include(":lamp-blog-file:file-api")
include(":lamp-blog-file:file-awss3")
include(":lamp-blog-user")
include(":lamp-blog-user:user-api")
include(":lamp-blog-user:staff-api")
include(":lamp-blog-user:staff-service")
include(":lamp-blog-user:user-details-api")
include(":lamp-blog-user:user-details-service")
include(":lamp-blog-user:user-service")
include(":lamp-blog-iam")
include(":lamp-blog-iam:authentication-api")
include(":lamp-blog-iam:authentication-service")
include(":lamp-blog-common")
include(":lamp-blog-common-data")
include(":lamp-blog-content")
include(":lamp-blog-content:content-api")
include(":lamp-blog-content:content-service")
include(":lamp-blog-content:article-service")
include(":lamp-blog-content:comment-service")
include(":lamp-blog-content:review-service")
include(":lamp-blog-system")
include(":lamp-blog-system:setting-api")
include(":lamp-blog-system:setting-service")
include(":lamp-blog-system:message-resource-api")
include(":lamp-blog-system:message-resource-service")

project(":lamp-blog-content:content-api").projectDir = file("lamp-blog-content/content-api")
project(":lamp-blog-content:content-service").projectDir = file("lamp-blog-content/content-service")
project(":lamp-blog-content:article-service").projectDir = file("lamp-blog-content/article-service")
project(":lamp-blog-content:comment-service").projectDir = file("lamp-blog-content/comment-service")
project(":lamp-blog-content:review-service").projectDir = file("lamp-blog-content/review-service")
project(":lamp-blog-user:user-api").projectDir = file("lamp-blog-user/user-api")
project(":lamp-blog-user:staff-api").projectDir = file("lamp-blog-user/staff-api")
project(":lamp-blog-user:staff-service").projectDir = file("lamp-blog-user/staff-service")
project(":lamp-blog-user:user-details-api").projectDir = file("lamp-blog-user/user-details-api")
project(":lamp-blog-user:user-details-service").projectDir = file("lamp-blog-user/user-details-service")
project(":lamp-blog-user:user-service").projectDir = file("lamp-blog-user/user-service")
project(":lamp-blog-file:file-api").projectDir = file("lamp-blog-file/file-api")
project(":lamp-blog-file:file-awss3").projectDir = file("lamp-blog-file/file-awss3")
project(":lamp-blog-iam:authentication-api").projectDir = file("lamp-blog-iam/authentication-api")
project(":lamp-blog-iam:authentication-service").projectDir = file("lamp-blog-iam/authentication-service")
project(":lamp-blog-system:setting-api").projectDir = file("lamp-blog-system/setting-api")
project(":lamp-blog-system:setting-service").projectDir = file("lamp-blog-system/setting-service")
project(":lamp-blog-system:message-resource-api").projectDir = file("lamp-blog-system/message-resource-api")
project(":lamp-blog-system:message-resource-service").projectDir = file("lamp-blog-system/message-resource-service")
