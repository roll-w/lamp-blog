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

const base = 'localhost:5100';

export const baseUrl = `http://${base}`;
export const wsBaseUrl = `ws://${base}`;

const prefix = `${baseUrl}/api/v1`;
const adminPrefix = `${baseUrl}/api/v1/admin`;

const passwordLogin = `${prefix}/user/login/password`;
const emailLogin = `${prefix}/user/login/email`;
const logout = `${prefix}/user/logout`;
const register = `${prefix}/user/register`;

const userInfo = (userId, admin = false) => {
    return `${admin ? adminPrefix : prefix}/user/${userId}`;
}

const allReviews = `${adminPrefix}/reviews`;
const reviews = `${adminPrefix}/reviews`;
const currentReviews = `${prefix}/reviews`;

const statusReviews = (userId, admin = false) =>
    `${admin ? adminPrefix : prefix}/${userId}/reviews`;
const reviewContent = (jobId, admin = false) =>
    `${admin ? adminPrefix : prefix}/review/${jobId}/content`;
const reviewResource = (jobId, admin = false) =>
    `${admin ? adminPrefix : prefix}/review/${jobId}`;


const tokenRefresh = `${prefix}/auth/token/r`;

const articleDetails = (userId, articleId, admin) => {
    return `${admin ? adminPrefix : prefix}/${userId}/article/${articleId}`;
}

const articles = (userId, admin) => {
    return `${admin ? adminPrefix : prefix}/${userId}/articles`;
}

const articleCreate2 = (userId, admin) => {
    return `${admin ? adminPrefix : prefix}/${userId}/article`;
}

const articleCreate = `${prefix}/article`;


const userList = `${adminPrefix}/users`;

const systemErrorLog = `${adminPrefix}/system/errors`;
const systemSettings = `${adminPrefix}/system/settings`;

export default {
    passwordLogin,
    emailLogin,
    logout,
    register,
    tokenRefresh,
    userInfo,

    articleCreate,
    articleDetails,
    articles,
    userList,

    allReviews,
    currentReviews,
    statusReviews,
    reviewContent,
    reviewResource,
    systemErrorLog,
    systemSettings,

}
