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

package tech.lamprism.lampray.user.repository

import org.springframework.data.jpa.repository.Query
import tech.lamprism.lampray.common.data.CommonDao
import tech.lamprism.lampray.common.data.Dao
import java.util.Optional

@Dao
@JvmDefaultWithoutCompatibility
interface UserDao : CommonDao<UserDo, Long> {
    @Query("SELECT u FROM UserDo u WHERE u.id = :id")
    fun getByUserId(id: Long): Optional<UserDo>

    @Query("SELECT u FROM UserDo u WHERE u.username = :username")
    fun getByUsername(username: String): Optional<UserDo>

    @Query("SELECT u FROM UserDo u WHERE u.email = :email")
    fun getByEmail(email: String): Optional<UserDo>
}