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

package space.lingu.lamp.web.domain.push.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;
import space.lingu.lamp.mail.util.MimeMailMessageBuilder;
import space.lingu.lamp.web.domain.push.PushMessageBody;
import space.lingu.lamp.web.domain.push.PushMessageStrategy;
import space.lingu.lamp.web.domain.push.PushType;
import space.lingu.lamp.web.domain.push.PushUser;
import space.lingu.lamp.user.UserIdentity;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class MailPushMessageStrategy implements PushMessageStrategy {
    private final JavaMailSender mailSender;

    public MailPushMessageStrategy(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean supports(PushType pushType) {
        return pushType == PushType.EMAIL;
    }

    @Override
    public void push(PushUser pushUser, UserIdentity target,
                     PushMessageBody pushMessageBody) {
        if (!(pushUser instanceof MailPushUser mailPushUser)) {
            throw new IllegalArgumentException("pushUser must be MailPushUser");
        }

        MimeMailMessage message = new MimeMailMessageBuilder(mailSender)
                .setTo(target.getEmail())
                .setFrom(mailPushUser.toSenderName())
                .setSubject(pushMessageBody.getTitle())
                .setText(pushMessageBody.getContent(),
                        pushMessageBody.getMimeType().isHtml())
                .build();
        mailSender.send(message.getMimeMessage());
    }

    @Override
    public void push(PushUser pushUser, List<UserIdentity> targets,
                     PushMessageBody pushMessageBody) {
        if (!(pushUser instanceof MailPushUser mailPushUser)) {
            throw new IllegalArgumentException("pushUser must be MailPushUser");
        }

        MimeMailMessage message = new MimeMailMessageBuilder(mailSender)
                .setTo(targets.stream()
                        .map(UserIdentity::getEmail)
                        .toArray(String[]::new))
                .setFrom(mailPushUser.toSenderName())
                .setSubject(pushMessageBody.getTitle())
                .setText(pushMessageBody.getContent(),
                        pushMessageBody.getMimeType().isHtml())
                .build();
        mailSender.send(message.getMimeMessage());
    }

}
