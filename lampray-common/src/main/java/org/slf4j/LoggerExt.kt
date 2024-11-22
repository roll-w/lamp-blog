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

package org.slf4j

import org.slf4j.event.Level

/**
 * @author RollW
 */
inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)

fun Logger.trace(message: () -> String) = logAtLevel(Level.TRACE, message)

fun Logger.debug(message: () -> String) = logAtLevel(Level.DEBUG, message)

fun Logger.info(message: () -> String) = logAtLevel(Level.INFO, message)

fun Logger.warn(message: () -> String) = logAtLevel(Level.WARN, message)

fun Logger.error(message: () -> String) = logAtLevel(Level.ERROR, message)

fun Logger.trace(t: Throwable, message: () -> String) = logAtLevel(Level.TRACE, message, t)

fun Logger.debug(t: Throwable, message: () -> String) = logAtLevel(Level.DEBUG, message, t)

fun Logger.info(t: Throwable, message: () -> String) = logAtLevel(Level.INFO, message, t)

fun Logger.warn(t: Throwable, message: () -> String) = logAtLevel(Level.WARN, message, t)

fun Logger.error(t: Throwable, message: () -> String) = logAtLevel(Level.ERROR, message, t)

private fun Logger.logAtLevel(level: Level, message: () -> String) {
    when (level) {
        Level.TRACE -> if (isTraceEnabled) trace(message())
        Level.DEBUG -> if (isDebugEnabled) debug(message())
        Level.INFO -> if (isInfoEnabled) info(message())
        Level.WARN -> if (isWarnEnabled) warn(message())
        Level.ERROR -> if (isErrorEnabled) error(message())
    }
}

private fun Logger.logAtLevel(level: Level, message: () -> String, t: Throwable) {
    when (level) {
        Level.TRACE -> if (isTraceEnabled) trace(message(), t)
        Level.DEBUG -> if (isDebugEnabled) debug(message(), t)
        Level.INFO -> if (isInfoEnabled) info(message(), t)
        Level.WARN -> if (isWarnEnabled) warn(message(), t)
        Level.ERROR -> if (isErrorEnabled) error(message(), t)
    }
}

