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

package space.lingu.lamp.web.domain.review.service;

import space.lingu.lamp.web.domain.review.dto.ReviewContent;
import space.lingu.lamp.web.domain.review.ReviewType;

/**
 * @author RollW
 */
public interface ReviewContentServiceStrategy {
    /**
     * Get the content of the review.
     *
     * @param contentId The content id.
     * @return The content of the review.
     */
    ReviewContent getContent(String contentId);
    // if type is image, returns the image url

    /**
     * Get the type of the review.
     *
     * @return The type of the review.
     */
    ReviewType reviewType();
}
