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

package space.lingu.lamp.web.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import space.lingu.lamp.web.common.MailConfigKeys;
import space.lingu.lamp.web.data.database.repository.SystemSettingRepository;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * @author RollW
 */
@Configuration
public class MailConfiguration {
    private final SystemSettingRepository systemSettingRepository;

    public MailConfiguration(SystemSettingRepository systemSettingRepository) {
        this.systemSettingRepository = systemSettingRepository;
    }

    @Bean
    public MailProperties mailProperties() {
        MailProperties properties = new MailProperties();
        properties.setDefaultEncoding(StandardCharsets.UTF_8);
        String host = systemSettingRepository.get(MailConfigKeys.KEY_SMTP_SERVER_HOST);
        if (host == null) {
            return properties;
        }
        Map<String, String> conf = properties.getProperties();
        conf.put("mail.smtp.auth", "true");
        conf.put("smtp.starttls.enable", "true");
        conf.put("smtp.starttls.required", "true");
        conf.put("smtp.ssl.enable", "true");

        String port = systemSettingRepository.get(MailConfigKeys.KEY_SMTP_SERVER_PORT);
        String username = systemSettingRepository.get(MailConfigKeys.KEY_MAIL_USERNAME);
        String password = systemSettingRepository.get(MailConfigKeys.KEY_MAIL_PASSWORD);
        properties.setHost(host);
        properties.setPort(Integer.parseInt(port));
        properties.setUsername(username);
        properties.setPassword(password);
        return properties;
    }

    // from springboot
    @Bean
    public JavaMailSender javaMailSender(MailProperties mailProperties) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        applyProperties(mailProperties, javaMailSender);
        return javaMailSender;
    }

    private static final Logger logger = LoggerFactory.getLogger(MailConfiguration.class);

    private void applyProperties(MailProperties properties, JavaMailSenderImpl sender) {
        logger.debug("Configure java mail sender, properties: {}", properties);
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort());
        }
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }
        if (!properties.getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(properties.getProperties()));
        }
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }
}
