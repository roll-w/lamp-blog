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
package space.lingu.lamp.setting

import com.google.common.base.Strings
import space.lingu.lamp.setting.SettingSpecification.Companion.keyName
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.Properties

/**
 * @author RollW
 */
class InputStreamConfigReader(inputStream: InputStream) : ConfigReader {
    private val properties: Properties = Properties()

    init {
        properties.load(inputStream)
    }

    override fun get(key: String): String? {
        return properties.getProperty(key, null)
    }

    override fun get(key: String, defaultValue: String?): String? {
        return properties.getProperty(key, defaultValue)
    }

    @JvmName("getOrDefault")
    @Suppress("INAPPLICABLE_JVM_NAME")
    override fun get(key: String, defaultValue: String): String {
        return properties.getProperty(key, defaultValue)
    }

    private fun <T, V> getRaw(specification: SettingSpecification<T, V>): T? {
        val value = properties.getProperty(specification.keyName) ?: return null
        return with(SettingSpecificationHelper) {
            value.deserialize(specification)
        }
    }

    override operator fun <T, V> get(spec: SettingSpecification<T, V>): T? {
        return getRaw(spec) ?: spec.defaultValue
    }

    override fun <T, V> get(spec: SettingSpecification<T, V>, defaultValue: T): T {
        return getRaw(spec) ?: defaultValue
    }

    override fun list(): List<RawSettingValue> {
        return properties.entries.map {
            RawSettingValue(
                it.key.toString(),
                it.value.toString(),
                RawSettingValue.Source.LOCAL
            )
        }
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        @Throws(IOException::class)
        fun loadConfig(
            appClz: Class<*>,
            path: String? = null,
            allowFail: Boolean = true
        ): InputStreamConfigReader {
            val inputStream = openConfigInput(appClz, path, allowFail)
                ?: InputStream.nullInputStream()
            return InputStreamConfigReader(inputStream)
        }

        private fun openConfigInput(
            appClz: Class<*>,
            path: String?,
            allowFail: Boolean
        ): InputStream? {
            val confFile = tryFile(path, allowFail)
            if (!confFile.exists()) {
                return appClz.getResourceAsStream("/lamp.conf")
            }
            return FileInputStream(confFile)
        }

        private fun tryFile(path: String?, allowFail: Boolean): File {
            if (!Strings.isNullOrEmpty(path)) {
                val givenFile = File(path!!)
                if (givenFile.exists()) {
                    return givenFile
                }
                if (!allowFail) {
                    throw FileNotFoundException(
                        "Given config file '$path' (absolute path: ${givenFile.absolutePath}) does not exist."
                    )
                }
            }
            val confFile = File("conf/lamp.conf")
            if (confFile.exists()) {
                return confFile
            }
            return File("lamp.conf")
        }
    }
}
