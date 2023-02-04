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
import LoginView from "@/views/LoginView.vue";
import UserLayout from "@/views/UserLayout.vue";

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
                    name: 'index',
                    component: () => import('@/views/UserIndex.vue'),
                    meta: {
                        title: "首页 | Lamp Blog"
                    }
                },
                {
                    path: '/login',
                    name: 'login-page',
                    component: () => import('@/views/LoginView.vue'),
                    meta: {
                        title: "登录 | Lamp Blog"
                    }
                },
                {
                    path: '/register',
                    name: 'register-page',
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
                    name: 'admin-index',
                    component: () => import('@/views/AdminIndex.vue'),
                },
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
