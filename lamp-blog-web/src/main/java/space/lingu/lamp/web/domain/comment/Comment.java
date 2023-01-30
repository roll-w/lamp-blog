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

package space.lingu.lamp.web.domain.comment;

import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

/**
 * @author RollW
 */
@DataTable(tableName = "comment")
public class Comment {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @DataColumn(name = "user_id")
    private long userId;

    @DataColumn(name = "content_id")
    private String contentId;

    @DataColumn(name = "parent_id")
    private Long parentId;

    @DataColumn(name = "content")
    private String content;

    @DataColumn(name = "create_time")
    private long createTime;

    @DataColumn(name = "deleted")
    private boolean deleted;

    @DataColumn(name = "type")
    private CommentType type;

    public Comment(Long id, long userId, String contentId,
                   Long parentId, String content, long createTime,
                   boolean deleted, CommentType type) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
        this.parentId = parentId;
        this.content = content;
        this.createTime = createTime;
        this.deleted = deleted;
        this.type = type;
    }


    // TODO: comment
}
