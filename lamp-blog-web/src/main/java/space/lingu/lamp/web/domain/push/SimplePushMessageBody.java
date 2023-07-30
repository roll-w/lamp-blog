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
public class SimplePushMessageBody implements PushMessageBody {
    private final String title;
    private final String content;
    private final MessageMimeType mimeType;

    public SimplePushMessageBody(String title, String content,
                                 MessageMimeType mimeType) {
        this.title = title;
        this.content = content;
        this.mimeType = mimeType;
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public MessageMimeType getMimeType() {
        return mimeType;
    }
}
