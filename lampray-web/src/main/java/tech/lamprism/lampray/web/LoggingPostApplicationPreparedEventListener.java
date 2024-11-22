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

package tech.lamprism.lampray.web;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import space.lingu.NonNull;
import tech.lamprism.lampray.logging.ColorConverter;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Set up logger context after the application environment
 * has been prepared.
 *
 * @author RollW
 */
public class LoggingPostApplicationPreparedEventListener implements
        ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
    public LoggingPostApplicationPreparedEventListener() {
    }

    @Override
    public void onApplicationEvent(
            @NonNull ApplicationEnvironmentPreparedEvent event
    ) {
        LoggerContext loggerContext = getLoggerContext();
        setupConversionRule(loggerContext);
        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(
                Logger.ROOT_LOGGER_NAME);
        setupConsoleAppender(rootLogger, loggerContext);
        setupFileAppender(rootLogger, loggerContext);
    }

    // TODO: support show full logger name by config
    // TODO: support set shown timezone or not by config

    private void setupConsoleAppender(ch.qos.logback.classic.Logger rootLogger,
                                      LoggerContext loggerContext) {
        // TODO: allow disable console appender by config
        ConsoleAppender<ILoggingEvent> consoleAppender = (ConsoleAppender<ILoggingEvent>)
                rootLogger.getAppender("CONSOLE");
        if (consoleAppender == null) {
            return;
        }
        // Stop the encoder to avoid a ch.qos.logback.core.encoder.EncoderBase error
        consoleAppender.getEncoder().stop();
        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setContext(loggerContext);

        String pattern = resolve(loggerContext,
                "%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd'T'HH:mm:ss.SSSXXX}}){faint} "
                        + "%clr(${LOG_LEVEL_PATTERN:-%5p}) "
                        + "%clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} "
                        + "%clr(${LOG_CORRELATION_PATTERN:-}){faint}%clr(%-50.50logger{49}){cyan} "
                        + "%clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}");
        patternLayoutEncoder.setPattern(pattern);
        patternLayoutEncoder.setParent(consoleAppender);
        patternLayoutEncoder.setCharset(resolveCharset(loggerContext, "${CONSOLE_LOG_CHARSET:-UTF-8}"));
        patternLayoutEncoder.start();

        consoleAppender.setEncoder(patternLayoutEncoder);
    }

    private void setupFileAppender(ch.qos.logback.classic.Logger rootLogger,
                                   LoggerContext loggerContext) {
        RollingFileAppender<ILoggingEvent> fileAppender = (RollingFileAppender<ILoggingEvent>)
                rootLogger.getAppender("FILE");
        if (fileAppender == null) {
            return;
        }
        fileAppender.getEncoder().stop();

        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setContext(loggerContext);
        String pattern = resolve(loggerContext,
                "%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd'T'HH:mm:ss.SSSXXX}} " +
                        "${LOG_LEVEL_PATTERN:-%5p} " +
                        "${PID:- } --- [%t] " +
                        "${LOG_CORRELATION_PATTERN:-}%-50.50logger{49} : " +
                        "%m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}");
        patternLayoutEncoder.setPattern(pattern);
        patternLayoutEncoder.setParent(fileAppender);
        patternLayoutEncoder.setCharset(resolveCharset(loggerContext, "${FILE_LOG_CHARSET:-UTF-8}"));
        patternLayoutEncoder.start();

        fileAppender.setEncoder(patternLayoutEncoder);
    }

    private Charset resolveCharset(LoggerContext context, String val) {
        return Charset.forName(resolve(context, val));
    }

    private String resolve(LoggerContext context, String val) {
        try {
            return OptionHelper.substVars(val, context);
        } catch (ScanException ex) {
            throw new RuntimeException(ex);
        }
    }

    private LoggerContext getLoggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @SuppressWarnings("unchecked")
    private void setupConversionRule(LoggerContext loggerContext) {
        Map<String, String> ruleRegistry = (Map<String, String>) loggerContext
                .getObject(CoreConstants.PATTERN_RULE_REGISTRY);
        if (ruleRegistry == null) {
            ruleRegistry = new HashMap<>();
            loggerContext.putObject(CoreConstants.PATTERN_RULE_REGISTRY, ruleRegistry);
        }
        ruleRegistry.put("clr", ColorConverter.class.getName());
    }

    @Override
    public int getOrder() {
        // to avoid the LoggingApplicationListener resetting the Logger,
        // set a higher order
        return LoggingApplicationListener.DEFAULT_ORDER + 1;
    }
}
