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

import {createRouter, createWebHistory} from 'vue-router'
import UserLayout from "@/views/UserLayout.vue";

export const index = "index"
export const login = "login-page"
export const register = "register-page"
export const admin = "admin-index"
export const adminUsers = "admin-user-list"
export const systemLog = "admin=system-log"

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/layout',
            name: 'user-layout',
            redirect: '/',
            component: UserLayout,
            children: [
                {
                    path: '/',
                    name: index,
                    component: () => import('@/views/UserIndex.vue'),
                    meta: {
                        title: "首页 | Lamp Blog"
                    }
                },
                {
                    path: '/login',
                    name: login,
                    component: () => import('@/views/LoginView.vue'),
                    meta: {
                        title: "登录 | Lamp Blog"
                    }
                },
                {
                    path: '/register',
                    name: register,
                    component: () => import('@/views/LoginView.vue'),
                    meta: {
                        title: "注册 | Lamp Blog"
                    }
                },

            ]
        },
        {
            path: '/layout/admin',
            name: 'admin-layout',
            redirect: '/admin',
            component: () => import('@/views/AdminLayout.vue'),
            children: [
                {
                    path: '/admin',
                    name: admin,
                    component: () => import('@/views/AdminIndex.vue'),
                    meta: {
                        title: "系统管理 | Lamp Blog"
                    }
                },
                {
                    path: '/admin/user',
                    name: adminUsers,
                    component: () => import('@/views/admin/UsersList.vue'),
                    meta: {
                        title: "用户管理 | Lamp Blog"
                    }
                },
                {
                    path: '/admin/system/logs',
                    name: systemLog,
                    component: () => import('@/views/admin/SystemLog.vue'),
                    meta: {
                        title: "系统日志 | Lamp Blog"
                    }
                }

            ]
        }

    ]
})

const defaultTitle = "Lamp Blog";

router.beforeEach((to, from, next) => {
    document.title = to.meta.title ? to.meta.title : defaultTitle
    next()
})

export default router
