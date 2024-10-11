# Lamp Blog

[![License][liBadge]][liLink]

A blog system built with Spring Boot 3 and Vue3.

## Requirements

- Java 17 or higher
- MySQL 8.0 or higher

This project also requires the following libraries that require you
to install to your local maven repository:

- [web-common](https://github.com/Roll-W/web-common-starter)

## Getting Started

> This part includes the steps to get the backend of the project running.
> For the frontend, please refer to the [frontend](lamp-blog-frontend/README.md).

### Building the Project

After cloning the repository, and installing the required libraries,
you can build the project using the following command:

```shell
./gradlew build
```

or if you want to skip the tests:

```shell
./gradlew assemble 
```

After building the project, you should be able to find the jar file in
`lamp-blog-web/build/libs` directory.

### Configuration

To start up the application, you need to provide a configuration file.

The configuration file uses the `properties` format, like the following:

```properties
# Database Configuration
database.url=jdbc:mysql://localhost:3306/lamp_blog
database.username=root
database.password=root

# Server Configuration
server.port=5100
```

### Running the Application

After building the project, then you can run the application
using the following command:

```shell
java -jar lamp-blog.jar  # Replace lamp-blog.jar with the actual jar file name
```

> Current support command line arguments:
> - `--config`, `-c`: Specify the configuration file to use.
> Default will try find `lamp.conf` in the current directory.

By default, the application will start on port `5100`. And database
related configurations must be provided in the configuration file,
or the application will fail to start.

## Features

- User Management
- Article Management

## Licence

```text
Copyright (C) 2023 RollW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[liBadge]: https://img.shields.io/github/license/roll-w/lamp-blog?color=569cd6&style=flat-square

[liLink]: https://github.com/roll-w/lamp-blog/blob/master/LICENSE
