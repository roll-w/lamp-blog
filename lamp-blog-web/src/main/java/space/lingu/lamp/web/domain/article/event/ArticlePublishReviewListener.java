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

package space.lingu.lamp.web.domain.article.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.service.ReviewService;

/**
 * @author RollW
 */
@Component
public class ArticlePublishReviewListener implements ApplicationListener<OnArticlePublishEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ArticlePublishReviewListener.class);

    private final ReviewService reviewService;

    public ArticlePublishReviewListener(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    @Async
    public void onApplicationEvent(@NonNull OnArticlePublishEvent event) {
        Article article = event.getArticle();
        logger.debug("Article publish event received: {}", article);
        if (article == null) {
            return;
        }
        if (!article.needsReview()) {
            return;
        }
        ReviewJob job = reviewService.assignReviewer(article);
        logger.debug("Article review job assigned: {}", job);
    }
}
