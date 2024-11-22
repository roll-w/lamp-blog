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

/**
 * @param {NotificationApiInjection} notification
 * @param {{tip: string, message: string}} error
 * @param {string} location
 * @param {string} errorType
 */
export function popAdminErrorTemplate(notification, error,
                                      location = "请求错误",
                                      errorType = "请求错误") {
    const msg = error.tip +
        "\n信息： " + error.message
    notification.error({
        title: location,
        content: msg,
        meta: errorType,
        duration: 3000,
        keepAliveOnHover: true
    })
}

/**
 * @param {NotificationApiInjection} notification
 * @param {{tip: string, message: string}} error
 * @param {string} location
 * @param {string} errorType
 */
export function popUserErrorTemplate(notification, error,
                                     location = "请求错误",
                                     errorType = "请求错误") {
    const msg = error.tip
    notification.error({
        title: location,
        content: msg,
        meta: errorType,
        duration: 3000,
        keepAliveOnHover: true
    })
}
