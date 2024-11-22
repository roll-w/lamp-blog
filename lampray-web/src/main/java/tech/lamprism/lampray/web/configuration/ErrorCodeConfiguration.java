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

package tech.lamprism.lampray.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.lamprism.lampray.content.article.common.ArticleErrorCode;
import tech.lamprism.lampray.content.comment.common.CommentErrorCode;
import tech.lamprism.lampray.content.common.ContentErrorCode;
import tech.lamprism.lampray.content.review.common.ReviewErrorCode;
import tech.rollw.common.web.AuthErrorCode;
import tech.rollw.common.web.DataErrorCode;
import tech.rollw.common.web.ErrorCodeFinderChain;
import tech.rollw.common.web.IoErrorCode;
import tech.rollw.common.web.UserErrorCode;
import tech.rollw.common.web.WebCommonErrorCode;

/**
 * @author RollW
 */
@Configuration
public class ErrorCodeConfiguration {

    @Bean
    public ErrorCodeFinderChain errorCodeFinderChain() {
        return ErrorCodeFinderChain.start(
                WebCommonErrorCode.getFinderInstance(),
                AuthErrorCode.getFinderInstance(),
                DataErrorCode.getFinderInstance(),
                IoErrorCode.getFinderInstance(),
                UserErrorCode.getFinderInstance(),
                ContentErrorCode.getFinderInstance(),
                ReviewErrorCode.getFinderInstance(),
                ArticleErrorCode.getFinderInstance(),
                CommentErrorCode.getFinderInstance()
        );
    }

}
