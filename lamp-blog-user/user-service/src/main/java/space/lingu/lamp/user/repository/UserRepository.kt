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
import org.springframework.stereotype.Repository
import space.lingu.lamp.common.data.CommonRepository
import java.util.Optional

/**
 * @author RollW
 */
@Repository
class UserRepository(
    private val userDao: UserDao
) : CommonRepository<UserDo, Long>(userDao) {

    fun searchBy(keyword: String): List<UserDo> {
        return userDao.findAll(Specs.searchBy(keyword))
    }

    fun getByUserId(id: Long): Optional<UserDo> = userDao.getByUserId(id)

    fun getByUsername(username: String): Optional<UserDo> = userDao.getByUsername(username)

    fun getByEmail(email: String): Optional<UserDo> = userDao.getByEmail(email)

    fun getByIds(ids: MutableList<Long>): List<UserDo> =
        userDao.findAllById(ids)

    fun isExistByEmail(email: String): Boolean {
        return getByEmail(email).isPresent
    }

    fun isExistByUsername(username: String): Boolean {
        return getByUsername(username).isPresent
    }

    fun hasUsers(): Boolean {
        return userDao.count() > 0
    }

    private object Specs {
        fun searchBy(keyword: String): Specification<UserDo> {
            return Specification { root, query, builder ->
                builder.like(root.get("username"), keyword)
            }
        }
    }
}