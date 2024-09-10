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

package space.lingu.lamp.web.command

import org.apache.commons.text.WordUtils
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import org.slf4j.logger
import org.slf4j.warn
import org.springframework.shell.Utils
import org.springframework.shell.command.CommandOption
import org.springframework.shell.command.CommandRegistration
import org.springframework.shell.standard.AbstractShellComponent
import org.springframework.shell.standard.CommandValueProvider
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.util.stream.Collectors
import java.util.stream.Stream

private const val LINE_LENGTH = 110

/**
 * @author RollW
 */
@ShellComponent
class Help : AbstractShellComponent() {

    @ShellMethod(
        value = "Display help about available commands.",
        group = CommandGroups.COMMON,
        key = ["help"]
    )
    fun help(
        @ShellOption(
            defaultValue = ShellOption.NULL,
            valueProvider = CommandValueProvider::class,
            value = ["-C", "--command"],
            help = "The command to obtain help for.",
            arity = Int.MAX_VALUE
        ) command: Array<String>?
    ): AttributedString = if (command == null) {
        renderCommands()
    } else {
        val commandStr = Stream.of(*command)
            .map { s -> s.trim { it <= ' ' } }
            .collect(Collectors.joining(" "))
        renderCommand(commandStr)
    }

    private fun renderCommands(): AttributedString {
        val registrations = Utils
            .removeHiddenCommands(commandCatalog.registrations)

        return AttributedStringBuilder()
            .apply {
                renderCommands(registrations)
            }
            .toAttributedString()
    }

    private fun AttributedStringBuilder.renderCommands(
        registrations: Map<String, CommandRegistration>
    ) {
        append("Lamp Blog Command Line Interface Help\n")
        append("\n")
        val groupded = registrations.groupByPrefixKey().entries.groupBy { it.value.group }
        for (group in groupded.keys) {
            append("$group: \n", AttributedStyle.DEFAULT)
            for (registration in groupded[group]!!) {
                append("")
                append("  ${registration.value.command.padEnd(14, ' ')}")
                registration.value.description.wrap(LINE_LENGTH - 16).lines().forEachIndexed { i, it ->
                    if (i == 0) {
                        appendLine(it)
                    } else {
                        appendLine("${" ".repeat(16)}$it")
                    }
                }
            }
            appendLine()
        }
        append("Use \"help <command>\" or \"<command> --help\" for more information about a given command.")
    }

    private fun Map<String, CommandRegistration>.groupByPrefixKey(): Map<String, CommandRegistration> = values
        .map { it.command.substringBefore(" ") }
        .distinct()
        .map {
            if (this[it] != null) {
                return@map it to this[it]!!
            }
            return@map it to CommandRegistration.builder()
                .command(it)
                .group("${it.replaceFirstChar(Char::titlecase)} Commands")
                .description("Base $it command.")
                .withTarget()
                .consumer {}
                .and()
                .build().also {
                    logger.warn { "Command '${it.command}' not registered, but has sub commands." }
                }
        }.toMap()

    private fun renderCommand(command: String): AttributedString {
        val registrations = Utils
            .removeHiddenCommands(commandCatalog.registrations)
        val registration = registrations.expandCommand(command)
            ?: return AttributedString("Unknown command '$command'")

        return AttributedStringBuilder().apply {
            renderCommand(registration)
        }.toAttributedString()
    }

    /**
     * Expand the command to include all subcommands (currently only one level deep)
     */
    private fun Map<String, CommandRegistration>.expandCommand(command: String): ExpandCommandRegistration? {
        val command = command.trim()
        fun hasAnyChild(command: String): Boolean {
            return this.any { it.key.startsWith("$command ") && it.key != command }
        }

        if (this[command] == null && !hasAnyChild(command)) {
            return null
        }
        val children = this.filter { it.key.startsWith(command) && it.key != command }
            .map { it.value }
            .sortedBy { it.command }
        return ExpandCommandRegistration(command, this[command], children)
    }

    private data class ExpandCommandRegistration(
        val command: String,
        val commandRegistration: CommandRegistration?,
        val children: List<CommandRegistration>
    )

    private fun AttributedStringBuilder.renderCommand(registration: ExpandCommandRegistration) {
        append("Usage:\n")
        // TODO: add required options
        val command = if (registration.children.isNotEmpty()) {
            "<command> [options]"
        } else {
            "[options]"
        }
        appendLine("  ${registration.command} ${command}\n")
        renderDescription(registration)
        renderChildrenCommands(registration)
        renderOptions(registration)
        append("Use \"help <command>\" or \"<command> --help\" for more information about a given command.")
    }

    private fun AttributedStringBuilder.renderDescription(
        registration: ExpandCommandRegistration
    ) {
        if (registration.commandRegistration == null) {
            return
        }
        registration.commandRegistration.description.wrap(110).lines().forEach {
            appendLine(it)
        }
        appendLine()
    }

    private fun AttributedStringBuilder.renderChildrenCommands(registration: ExpandCommandRegistration) {
        if (registration.children.isEmpty()) {
            return
        }
        appendLine("Available Commands:")
        registration.children.forEach {
            append("  ${it.command.removePrefix(registration.command).padEnd(14, ' ')}")
            it.description.wrap(LINE_LENGTH - 16).lines().forEachIndexed { i, it ->
                if (i == 0) {
                    appendLine(it)
                } else {
                    appendLine("${" ".repeat(16)}$it")
                }
            }
        }
        appendLine()
    }

    private fun List<CommandOption>.isOnlyHelp(): Boolean {
        return size == 1 && this.any { it.longNames.any { it == "help" } }
    }

    private fun AttributedStringBuilder.renderOptions(registration: ExpandCommandRegistration?) {
        val registration = registration?.commandRegistration ?: return
        if (registration.options.isEmpty() || registration.options.isOnlyHelp()) {
            return
        }
        appendLine("Options:")
        registration.options.forEach {
            if (it.longNames.any { it == "help" }) {
                return@forEach
            }
            append("    ${it.longNames.joinToString(", ", prefix = "--")}")
            if (it.shortNames.isNotEmpty()) {
                append(", ")
                append(it.shortNames.joinToString(", ", prefix = "-"))
            }
            if (!it.isRequired && it.defaultValue != null) {
                append("=${it.defaultValue}")
            }
            if (it.description?.isNotEmpty() == true) {
                appendLine(":")
                it.description.wrap(LINE_LENGTH - 8).lines().forEach {
                    appendLine("${" ".repeat(8)}$it")
                }
            }
            append("\n")
        }
    }

    private fun String.wrap(length: Int): String {
        return WordUtils.wrap(this, length)
    }

    private fun AttributedStringBuilder.appendLine(line: String, style: AttributedStyle = AttributedStyle.DEFAULT) {
        append("$line\n", style)
    }

    companion object {
        private val logger = logger<Help>()
    }
}