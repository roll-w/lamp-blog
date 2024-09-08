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

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import space.lingu.lamp.setting.ConfigProvider;
import space.lingu.lamp.setting.ConfigReader;
import space.lingu.lamp.web.common.keys.MailConfigKeys;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * @author RollW
 */
@Configuration
public class MailConfiguration {
    private final ConfigReader configReader;

    public MailConfiguration(ConfigProvider configReader) {
        this.configReader = configReader;
    }

    @Bean
    @Primary
    public MailProperties mailProperties() {
        MailProperties properties = new MailProperties();
        properties.setDefaultEncoding(StandardCharsets.UTF_8);
        setProperties(properties, configReader);
        return properties;
    }

    public static void setProperties(MailProperties properties, ConfigReader configReader) {
        Map<String, String> conf = properties.getProperties();
        conf.put("mail.smtp.auth", "true");
        conf.put("mail.smtp.starttls.enable", "false");
        conf.put("mail.smtp.starttls.required", "false");
        String sslEnable = configReader.get(MailConfigKeys.KEY_SMTP_SSL_ENABLE);
        conf.put("mail.smtp.ssl.enable", String.valueOf(Boolean.parseBoolean(sslEnable)));

        String host = configReader.get(MailConfigKeys.KEY_SMTP_SERVER_HOST);
        String port = configReader.get(MailConfigKeys.KEY_SMTP_SERVER_PORT);
        String username = configReader.get(MailConfigKeys.KEY_MAIL_USERNAME);
        String password = configReader.get(MailConfigKeys.KEY_MAIL_PASSWORD);
        properties.setHost(host);
        if (Strings.isNullOrEmpty(port)) {
            properties.setPort(25);
        } else {
            properties.setPort(Integer.parseInt(port));
        }
        String senderName = chooseUsername(username, configReader);
        properties.setUsername(senderName);
        properties.setPassword(password);
    }

    // from springboot
    @Bean
    @Primary
    public JavaMailSenderImpl javaMailSender(MailProperties mailProperties) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        applyProperties(mailProperties, javaMailSender);
        return javaMailSender;
    }

    private static final Logger logger = LoggerFactory.getLogger(MailConfiguration.class);

    protected static void applyProperties(MailProperties properties, JavaMailSenderImpl sender) {
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

    private static Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

    private static String chooseUsername(String username, ConfigReader configReader) {
        String sender = configReader.get(MailConfigKeys.KEY_MAIL_SENDER_NAME);
        if (Strings.isNullOrEmpty(sender)) {
            return username;
        }
        return sender + " <" + username + ">";
    }
}
