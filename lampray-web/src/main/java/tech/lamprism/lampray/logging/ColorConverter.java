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

package tech.lamprism.lampray.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiElement;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Logback {@link CompositeConverter} to color output using the {@link AnsiOutput} class.
 * A single 'color' option can be provided to the converter, or if not specified color
 * will be picked based on the logging level.
 */
public class ColorConverter extends CompositeConverter<ILoggingEvent> {

    private static final Map<String, AnsiElement> ELEMENTS;

    static {
        Map<String, AnsiElement> ansiElements = new HashMap<>();
        Arrays.stream(AnsiColor.values())
                .filter((color) -> color != AnsiColor.DEFAULT)
                .forEach((color) -> ansiElements.put(color.name().toLowerCase(), color));
        ansiElements.put("faint", AnsiStyle.FAINT);
        ELEMENTS = Collections.unmodifiableMap(ansiElements);
    }

    private static final Map<Integer, AnsiElement> LEVELS;

    static {
        Map<Integer, AnsiElement> ansiLevels = new HashMap<>();
        ansiLevels.put(Level.ERROR_INTEGER, AnsiColor.RED);
        ansiLevels.put(Level.WARN_INTEGER, AnsiColor.YELLOW);
        ansiLevels.put(Level.INFO_INTEGER, AnsiColor.BLUE);
        ansiLevels.put(Level.DEBUG_INTEGER, AnsiColor.GREEN);
        ansiLevels.put(Level.TRACE_INTEGER, AnsiColor.DEFAULT);
        LEVELS = Collections.unmodifiableMap(ansiLevels);
    }

    @Override
    protected String transform(ILoggingEvent event, String in) {
        AnsiElement color = ELEMENTS.get(getFirstOption());
        if (color == null) {
            // Assume highlighting
            color = LEVELS.get(event.getLevel().toInteger());
            color = (color != null) ? color : AnsiColor.GREEN;
        }
        return toAnsiString(in, color);
    }

    protected String toAnsiString(String in, AnsiElement element) {
        return AnsiOutput.toString(element, in);
    }

    static String getName(AnsiElement element) {
        return ELEMENTS.entrySet()
                .stream()
                .filter((entry) -> entry.getValue().equals(element))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }
}
