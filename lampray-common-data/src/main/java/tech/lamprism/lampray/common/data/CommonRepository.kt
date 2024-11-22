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

package tech.lamprism.lampray.common.data

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.NoRepositoryBean
import tech.lamprism.lampray.DataEntity
import tech.lamprism.lampray.common.data.PageUtils.toPage
import tech.rollw.common.web.page.Page

/**
 * @author RollW
 */
@NoRepositoryBean
abstract class CommonRepository<T : DataEntity<ID>, ID>(
    protected val commonDao: CommonDao<T, ID>
): CommonDao<T, ID> by commonDao {
    fun findAll(
        pageable: Pageable,
        spec: Specification<T>
    ): Page<T> = findAll(spec, pageable).toPage()

}