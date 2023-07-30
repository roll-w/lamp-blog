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

package space.lingu.lamp.web.domain.push;

/**
 * @author RollW
 */
public interface PushMessageBuilder {
    PushMessageBuilder setTitle(String title);

    PushMessageBuilder clearContent();

    PushMessageBuilder appendParagraph(String paragraph);

    PushMessageBuilder appendParagraphTitle(String paragraphTitle,
                                            ParagraphTitleLevel level);

    PushMessageBuilder appendContent(String content);

    PushMessageBuilder breakLine();

    PushMessageBuilder appendLink(String link, String linkText);

    PushMessageBuilder appendImage(byte[] image, String imageText);

    PushMessageBuilder appendImage(String imageUrl, String imageText);

    PushMessageBody build();
}
