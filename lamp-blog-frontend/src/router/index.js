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
import {useUserStore} from "@/stores/user";

export const index = "index"
export const login = "login-page"
export const register = "register-page"
export const registerActivate = "register-activate"
export const registerTip = "register-tip"
export const userPage = "user-page"
export const articleEditorPage = "article-page"
export const articleView = "article-details"
export const admin = "admin-index"
export const adminUsers = "admin-user-list"
export const adminReviews = "admin-review-list"
export const reviewsQueue = "admin-review-queue"
export const reviewerReviews = "admin-reviewer-review-list"
export const systemLog = "admin-system-log"
export const systemSettings = "admin-system-settings"
export const page404 = "404"

const adminRoutes = [
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
                component: () => import('@/views/admin/user/UsersList.vue'),
                meta: {
                    title: "用户管理 | Lamp Blog"
                }
            },
            {
                path: '/admin/system/logs',
                name: systemLog,
                component: () => import('@/views/admin/system/SystemLog.vue'),
                meta: {
                    title: "系统日志 | Lamp Blog"
                }
            }

        ]
    }
]

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
                        title: "首页"
                    }
                },
                {
                    path: '/user/login',
                    name: login,
                    component: () => import('@/views/LoginView.vue'),
                    meta: {
                        title: "登录"
                    }
                },
                {
                    path: '/user/register',
                    name: register,
                    component: () => import('@/views/LoginView.vue'),
                    meta: {
                        title: "注册"
                    }
                },
                {
                    path: '/user/register/activate/:token',
                    name: registerActivate,
                    component: () => import('@/views/user/RegisterActivation.vue'),
                    meta: {
                        title: "激活账户"
                    }
                },
                {
                    path: '/user/register/activate',
                    name: registerTip,
                    component: () => import('@/views/user/RegisterTips.vue'),
                    meta: {
                        title: "注册"
                    }
                },
                {
                    path: '/user/space/:id',
                    name: userPage,
                    component: () => import('@/views/user/UserPersonalPage.vue'),
                    meta: {
                        title: "用户主页"
                    }
                },

                {
                    path: '/:user/article/:id',
                    name: articleView,
                    component: () => import('@/views/article/ArticleViewer.vue'),
                    meta: {
                        title: "文章查看"
                    }
                },

                {
                    path: '/article/editor/:id',
                    name: articleEditorPage,
                    component: () => import('@/views/article/ArticleEditor.vue'),
                    meta: {
                        title: "文章编辑"
                    }
                },
                {
                    path: '/404',
                    name: page404,
                    component: () => import('@/views/NotFound.vue'),
                    meta: {
                        title: "404 | Lamp Blog"
                    }
                },
                {
                    path: '/:path(.*)*',
                    redirect: '/404'
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
                        title: "系统管理"
                    }
                },
                {
                    path: '/admin/user',
                    name: adminUsers,
                    component: () => import('@/views/admin/user/UsersList.vue'),
                    meta: {
                        title: "用户管理"
                    }
                },
                {
                    path: '/admin/reviews/reviewer',
                    name: reviewerReviews,
                    component: () => import('@/views/admin/review/ReviewJobs.vue'),
                    meta: {
                        title: "审核记录"
                    }
                },
                {
                    path: '/admin/reviews/manage',
                    name: adminReviews,
                    component: () => import('@/views/admin/review/ReviewJobs.vue'),
                    meta: {
                        title: "管理审核记录"
                    }
                },
                {
                    path: '/admin/reviews/queue',
                    name: reviewsQueue,
                    component: () => import('@/views/admin/review/ReviewJobQueue.vue'),
                    meta: {
                        title: "审核队列"
                    }
                },
                {
                    path: '/admin/reviews/:id',
                    meta: {
                        title: "审核详情"
                    }
                },
                {
                    path: '/admin/system/logs',
                    name: systemLog,
                    component: () => import('@/views/admin/system/SystemLog.vue'),
                    meta: {
                        title: "系统日志"
                    }
                },
                {
                    path: '/admin/system/settings',
                    name: systemSettings,
                    component: () => import('@/views/admin/system/SystemSettings.vue'),
                    meta: {
                        title: "系统设置"
                    }
                },
            ]
        }

    ]
})

const defaultTitle = "Lamp Blog";

export const getTitleSuffix = () => {
    return " | 灯客 - Lamp Blog"
}

router.beforeEach((to, from, next) => {
    document.title = to.meta.title ? to.meta.title + getTitleSuffix() : defaultTitle
    next()
})

router.beforeEach((to, from, next) => {
    const userStore = useUserStore()
    if (!to.name.startsWith("admin")) {
        return next()
    }
    const role = userStore.user.role
    if (!userStore.isLogin || !role || role.value === "USER") {
        return next({
            name: page404
        })
    }
    return next()
})

export default router
