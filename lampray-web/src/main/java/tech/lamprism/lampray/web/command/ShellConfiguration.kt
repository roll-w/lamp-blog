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

package tech.lamprism.lampray.web.command

import org.jline.reader.LineReader
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.shell.Shell
import org.springframework.shell.boot.ShellRunnerAutoConfiguration
import org.springframework.shell.boot.SpringShellProperties
import org.springframework.shell.boot.StandardCommandsAutoConfiguration
import org.springframework.shell.command.annotation.CommandScan
import org.springframework.shell.context.ShellContext
import org.springframework.shell.jline.InteractiveShellRunner
import org.springframework.shell.jline.PromptProvider

/**
 * @author RollW
 */
@Configuration
@EnableAutoConfiguration(
    exclude = [
        StandardCommandsAutoConfiguration::class,
        ShellRunnerAutoConfiguration::class
    ]
)
@CommandScan("tech.lamprism.lampray")
class ShellConfiguration {
    @Bean
    @Primary
    fun springShellProperties(): SpringShellProperties = SpringShellProperties().apply {
        interactive = interactive.apply {
            isEnabled = true
        }
        noninteractive = noninteractive.apply {
            isEnabled = false
        }
    }

    @Bean
    @Primary
    fun interactiveApplicationRunner(
        lineReader: LineReader,
        promptProvider: PromptProvider,
        shell: Shell,
        shellContext: ShellContext
    ): InteractiveShellRunner {
        return InteractiveShellRunner(lineReader, promptProvider, shell, shellContext)
    }
}