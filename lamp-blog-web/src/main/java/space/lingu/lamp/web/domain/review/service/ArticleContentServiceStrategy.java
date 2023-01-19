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

import org.springframework.stereotype.Service;
import space.lingu.lamp.web.domain.review.ReviewType;
import space.lingu.lamp.web.domain.review.dto.ReviewContent;

/**
 * @author RollW
 */
@Service
public class ArticleContentServiceStrategy implements ReviewContentServiceStrategy {
    @Override
    public ReviewContent getContent(String contentId) {
        long id = Long.parseLong(contentId);
        // TODO: get article content
        return new ReviewContent(
                "",
                "",
                ReviewType.ARTICLE);
    }

    @Override
    public ReviewType getReviewType() {
        return ReviewType.ARTICLE;
    }
}
