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

import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

/**
 * @author RollW
 */
public class SimpleMailMessageBuilder {
    private String[] to;
    private String text;
    private String subject;
    private Date sendDate;
    private String from;

    public SimpleMailMessageBuilder() {
    }

    public SimpleMailMessageBuilder setTo(String to) {
        setTo(new String[]{to});
        return this;
    }

    public SimpleMailMessageBuilder setTo(String[] to) {
        this.to = to;
        return this;
    }

    public SimpleMailMessageBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public SimpleMailMessageBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public SimpleMailMessageBuilder setFrom(String from) {
        this.from = from;
        return this;
    }

    public SimpleMailMessageBuilder setSendDate(Date sendDate) {
        this.sendDate = sendDate;
        return this;
    }

    public SimpleMailMessage build() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setText(text);
        mailMessage.setSubject(subject);
        mailMessage.setFrom(from);
        if (sendDate != null) {
            mailMessage.setSentDate(sendDate);
        }
        return mailMessage;
    }
}
