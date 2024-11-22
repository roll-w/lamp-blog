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

package org.springframework.expression.spel.support

import org.springframework.util.ClassUtils

/**
 * A type locator that gets a class by its simple name.
 *
 * It is useful when we don't want to include all the
 * classes under one package.
 *
 * @author RollW
 */
class SimpleNameTypeLocator @JvmOverloads constructor(
    classes: List<Class<*>> = emptyList(),
    classLoader: ClassLoader? = ClassUtils.getDefaultClassLoader()
) : StandardTypeLocator(classLoader) {
    private val simpleNames: MutableMap<String, Class<*>> = mutableMapOf()

    init {
        classes.forEach {
            simpleNames[it.simpleName] = it
        }
    }

    override fun findType(typeName: String): Class<*> {
        return simpleNames[typeName] ?: super.findType(typeName)
    }

    fun registerClass(clazz: Class<*>) {
        simpleNames[clazz.simpleName] = clazz
    }

    fun removeClass(clazz: Class<*>) {
        simpleNames.remove(clazz.simpleName)
    }
}