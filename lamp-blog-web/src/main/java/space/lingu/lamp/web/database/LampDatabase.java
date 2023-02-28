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

package space.lingu.lamp.web.database;

import space.lingu.lamp.web.database.dao.ArticleDao;
import space.lingu.lamp.web.database.dao.CommentDao;
import space.lingu.lamp.web.database.dao.ContentMetadataDao;
import space.lingu.lamp.web.database.dao.MessageResourceDao;
import space.lingu.lamp.web.database.dao.RegisterVerificationTokenDao;
import space.lingu.lamp.web.database.dao.ReviewJobDao;
import space.lingu.lamp.web.database.dao.StaffDao;
import space.lingu.lamp.web.database.dao.SystemSettingDao;
import space.lingu.lamp.web.database.dao.UserDao;
import space.lingu.lamp.web.database.dao.UserPersonalDataDao;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.comment.Comment;
import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionMetadata;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.staff.Staff;
import space.lingu.lamp.web.domain.user.RegisterVerificationToken;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import space.lingu.lamp.web.system.MessageResource;
import space.lingu.lamp.web.system.SystemSetting;
import space.lingu.light.DataConverters;
import space.lingu.light.Database;
import space.lingu.light.LightConfiguration;
import space.lingu.light.LightDatabase;

/**
 * @author RollW
 */
@Database(name = "lamp_blog_database", version = 1, tables = {
        User.class, UserPersonalData.class, Staff.class,
        RegisterVerificationToken.class,
        SystemSetting.class, MessageResource.class,
        Article.class,
        ReviewJob.class,
        Comment.class,
        ContentMetadata.class, ContentCollectionMetadata.class,
})
@DataConverters({LampConverter.class})
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "255")
public abstract class LampDatabase extends LightDatabase {
    public abstract UserDao getUserDao();

    public abstract UserPersonalDataDao getUserPersonalDataDao();

    public abstract RegisterVerificationTokenDao getRegisterVerificationTokenDao();

    public abstract SystemSettingDao getSystemSettingDao();

    public abstract MessageResourceDao getMessageResourceDao();

    public abstract ArticleDao getArticleDao();

    public abstract ReviewJobDao getReviewJobDao();

    public abstract StaffDao getStaffDao();

    public abstract ContentMetadataDao getContentMetadataDao();

    public abstract CommentDao getCommentDao();
}
