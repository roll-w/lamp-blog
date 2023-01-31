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

package space.lingu.lamp.web.domain.content.event;

import space.lingu.lamp.web.domain.content.Content;

/**
 * Receive this event don't change the content status.
 *
 * @author RollW
 */
public class ContentPublishEvent<C extends Content> extends ContentStatusEvent<C> {
    private final C content;
    private final PublishEventStage stage;

    public ContentPublishEvent(C content,
                               long timestamp,
                               PublishEventStage stage) {
        super(content, timestamp, null, stage.toStatus());
        this.content = content;
        this.stage = stage;
    }

    public C getContent() {
        return content;
    }

    public PublishEventStage getStage() {
        return stage;
    }

    public boolean needsAssign() {
        return stage.needsAssign();
    }

    public boolean needsReview() {
        return stage.needsReview();
    }
}
