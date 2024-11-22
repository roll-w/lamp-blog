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

/**
 * @author RollW
 */
public enum MessageMimeType {
    PLAIN_TEXT("text/plain"),
    HTML("text/html"),
    MARKDOWN("text/markdown"),
    NONE("application/octet-stream"),
    ;

    private final String mimeType;

    MessageMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public boolean isHtml() {
        return this == HTML;
    }

    public static MessageMimeType fromMimeType(String mimeType) {
        for (MessageMimeType value : values()) {
            if (value.getMimeType().equals(mimeType)) {
                return value;
            }
        }
        return NONE;
    }
}
