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

package tech.lamprism.lampray.content;

import com.google.common.base.Preconditions;
import space.lingu.Nullable;
import tech.rollw.common.web.system.SimpleSystemResourceKind;
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
            case ARTICLE -> new SimpleSystemResourceKind("ARTICLE");
            case COMMENT -> new SimpleSystemResourceKind("COMMENT");
            case POST -> new SimpleSystemResourceKind("POST");
            case IMAGE -> new SimpleSystemResourceKind("IMAGE");
        };
    }

    public static ContentType from(SystemResourceKind kind) {
        return switch (kind.getName()) {
            case "ARTICLE" -> ARTICLE;
            case "COMMENT" -> COMMENT;
            case "POST" -> POST;
            case "IMAGE" -> IMAGE;
            default -> null;
        };
    }

    @Nullable
    public static ContentType findByName(String name) {
        for (ContentType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public interface Type {
        ContentType getContentType();
    }
}
