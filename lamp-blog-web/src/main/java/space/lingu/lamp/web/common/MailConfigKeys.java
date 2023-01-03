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

package space.lingu.lamp.web.common;

/**
 * @author RollW
 */
public final class MailConfigKeys {
    public static final String EMAIL_DISABLED = "disable";

    public static final String KEY_SMTP_SERVER_HOST = "mail.smtp.server.host";
    public static final String KEY_SMTP_SERVER_PORT = "mail.smtp.server.port";
    public static final String KEY_MAIL_USERNAME = "mail.username";
    public static final String KEY_MAIL_PASSWORD = "mail.password";
    public static final String KEY_MAIL_SENDER_NAME = "mail.sender.name";

    public static final String PREFIX = "mail.";

    public static boolean isDisabled(String username) {
        return EMAIL_DISABLED.equals(username);
    }

    private MailConfigKeys() {
    }
}
