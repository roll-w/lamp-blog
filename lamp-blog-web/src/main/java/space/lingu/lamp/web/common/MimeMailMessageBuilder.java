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

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author RollW
 */
public class MimeMailMessageBuilder {
    private final MimeMessageHelper helper;
    private boolean html = false;
    private String[] to;
    private String text;
    private String subject;
    private Date sendDate;
    private String from;

    public MimeMailMessageBuilder(JavaMailSender javaMailSender) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        helper = new MimeMessageHelper(mimeMessage);
    }

    public MimeMailMessageBuilder(MimeMessageHelper helper) {
        this.helper = helper;
    }

    public MimeMailMessageBuilder setTo(String to) {
        setTo(new String[]{to});
        return this;
    }

    public MimeMailMessageBuilder setTo(String[] to) {
        this.to = to;
        return this;
    }

    public MimeMailMessageBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public MimeMailMessageBuilder setText(String text, boolean html) {
        this.text = text;
        this.html = html;
        return this;
    }

    public MimeMailMessageBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public MimeMailMessageBuilder setFrom(String from) {
        this.from = from;
        return this;
    }

    public MimeMailMessageBuilder setSendDate(Date sendDate) {
        this.sendDate = sendDate;
        return this;
    }

    public MimeMailMessage build() {
        try {
            helper.setTo(to);
            helper.setText(text, html);
            helper.setSubject(subject);
            helper.setFrom(from);
            if (sendDate != null) {
                helper.setSentDate(sendDate);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return new MimeMailMessage(helper);
    }
}
