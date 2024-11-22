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

package tech.lamprism.lampray.push;

import com.google.common.io.BaseEncoding;

import java.text.MessageFormat;

/**
 * @author RollW
 */
public class MarkdownMessageBuilder implements PushMessageBuilder {
    private String title;
    private StringBuilder content;
    
    public MarkdownMessageBuilder() {
        this.content = new StringBuilder();
    }
    
    @Override
    public MarkdownMessageBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public MarkdownMessageBuilder clearContent() {
        this.content = new StringBuilder();
        return this;
    }

    @Override
    public MarkdownMessageBuilder appendParagraph(String paragraph) {
        breakLine();
        content.append(paragraph);
        return this;
    }

    @Override
    public MarkdownMessageBuilder appendParagraphTitle(String paragraphTitle,
                                                   ParagraphTitleLevel level) {
        breakLine();
        content.append("#".repeat(level.getLevel()))
                .append(" ")
                .append(paragraphTitle);
        return this;
    }

    @Override
    public MarkdownMessageBuilder appendContent(String content) {
        return appendContent(content, false);
    }

    @Override
    public MarkdownMessageBuilder appendContent(String content,
                                                boolean escape) {
        if (escape) {
            content = escapeMarkdown(content);
        }
        this.content.append(content);
        return this;
    }

    @Override
    public MarkdownMessageBuilder appendContent(String content,
                                                Object... args) {
        this.content.append(MessageFormat.format(content, args));
        return this;
    }

    @Override
    public MarkdownMessageBuilder breakLine() {
        content.append("\n\n");
        return this;
    }

    @Override
    public MarkdownMessageBuilder appendLink(String link, String linkText) {
        content.append("[").append(linkText).append("](").append(link).append(")");
        return this;
    }

    @Override
    public MarkdownMessageBuilder appendImage(byte[] image, String imageText) {
        content.append("![")
                .append(imageText)
                .append("](")
                .append("data:image/png;base64,")
                .append(BaseEncoding.base64().encode(image))
                .append(")");
        return this;
    }

    @Override
    public MarkdownMessageBuilder appendImage(String imageUrl, String imageText) {
        content.append("![")
                .append(imageText)
                .append("](")
                .append(imageUrl)
                .append(")");
        return this;
    }

    @Override
    public PushMessageBody build() {
        return new SimplePushMessageBody(
                title, content.toString(), MessageMimeType.MARKDOWN);
    }

    private static String escapeMarkdown(String content) {
        return content;
    }
}
