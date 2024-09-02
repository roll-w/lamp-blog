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

package space.lingu.lamp.user.repository

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
@JvmDefaultWithoutCompatibility
interface UserDao : JpaRepository<UserDo, Long>, JpaSpecificationExecutor<UserDo> {
    @Query("SELECT u FROM UserDo u WHERE u.id = :id")
    fun getByUserId(id: Long): UserDo?

    @Query("SELECT u FROM UserDo u WHERE u.username = :username")
    fun getByUsername(username: String): UserDo?

    @Query("SELECT u FROM UserDo u WHERE u.email = :email")
    fun getByEmail(email: String): UserDo?

    fun searchBy(keyword: String): List<UserDo> {
        return findAll(Specs.searchBy(keyword))
    }

    fun getByIds(ids: MutableList<Long>): List<UserDo> {
        return findAllById(ids)
    }

    fun isExistByEmail(email: String): Boolean {
        return getByEmail(email) != null
    }

    fun isExistByUsername(username: String): Boolean {
        return getByUsername(username) != null
    }

    private object Specs {
        fun searchBy(keyword: String): Specification<UserDo> {
            return Specification { root, query, builder ->
                builder.like(root.get("username"), keyword)
            }
        }
    }
}