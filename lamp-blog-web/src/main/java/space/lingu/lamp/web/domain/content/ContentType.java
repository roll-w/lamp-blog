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

package space.lingu.lamp.web.domain.content;

import com.google.common.base.Preconditions;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import tech.rollw.common.web.system.SystemResourceKind;

/**
 * @author RollW
 */
public enum ContentType implements SystemResourceKind.Kind {
    /**
     * Article.
     */
    ARTICLE(3),
    /**
     * Comment.
     */
    COMMENT(1),
    /**
     * Post.
     */
    POST(1),
    /**
     * Image.
     */
    IMAGE(1),
    ;

    /**
     * The weight of the review type.
     */
    private final int weight;

    ContentType(int weight) {
        Preconditions.checkArgument(weight > 0, "Weight must be positive.");
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public SystemResourceKind getSystemResourceKind() {
        return switch (this) {
            case ARTICLE -> LampSystemResourceKind.ARTICLE;
            case COMMENT -> LampSystemResourceKind.COMMENT;
            case POST -> LampSystemResourceKind.POST;
            case IMAGE -> LampSystemResourceKind.IMAGE;
        };
    }
}
