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
package space.lingu.lamp.web

import org.springframework.boot.Banner
import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.ansi.AnsiOutput
import org.springframework.boot.ansi.AnsiStyle
import org.springframework.core.env.Environment
import java.io.PrintStream

/**
 * @author RollW
 */
class LampBlogBanner : Banner {
    companion object {
        val BANNER: String = """
                __
               / /   ____ _____ ___  ____
              / /   / __ `/ __ `__ \/ __ \
             / /___/ /_/ / / / / / / /_/ /
            /_____/\__,_/_/ /_/ /_/ .___/
                                 /_/ 

			""".trimIndent()

        const val LAMP_BLOG = " Lamp Blog"

        const val LICENSE = " Release under the Apache License, Version 2.0 (Apache-2.0)"

        const val STRAP_LINE_SIZE = LICENSE.length
    }

    override fun printBanner(
        environment: Environment,
        sourceClass: Class<*>,
        printStream: PrintStream
    ) {
        printStream.run {
            println()
            println(BANNER)
            // TODO: version read from configuration
            val version = " (v${"0.0.1"})"
            val padding = StringBuilder()
            while (padding.length < STRAP_LINE_SIZE - (version.length + LAMP_BLOG.length)) {
                padding.append(" ")
            }
            println(
                AnsiOutput.toString(
                    AnsiColor.YELLOW, LAMP_BLOG, AnsiColor.DEFAULT, padding.toString(),
                    AnsiStyle.FAINT, version
                )
            )
            println(LICENSE)
            println()
        }
    }
}
