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

import {defineStore} from 'pinia'

export const tokenKey = window.btoa("L2w9t0k3n")
export const userKey = "user"
export const userDataKey = "user_data"

export const useUserStore = defineStore('user', {
    state: () => ({
        /** @type {{ username: string, id: number, role: string }} */
        user: {},
        /** @type {string} */
        token: "",
        /** @type {{ avatar: string, nickname: string, setup: boolean }} */
        userData: {},
        remember: false,
    }),
    getters: {
        /** @return {boolean} */
        isLogin: state => state.token !== "",
        /** @return {{ username: string, id: number, role: string }} */
        getUser: state => state.user,
        /** @return {string} */
        getToken: state => state.token,

        getUserData: state => state.userData,

        canAccessAdmin: state => state.user.role && state.user.role !== "USER",
    },
    actions: {
        /**
         * @param {{ username: string, id: number, role: string }} user
         * @param {string} token
         * @param {boolean} remember
         */
        loginUser(user, token, remember) {
            this.user = user
            this.token = token
            this.remember = remember
        },

        logout() {
            this.user = {}
            this.token = ""
            this.userData = {}
            localStorage.removeItem(tokenKey)
            localStorage.removeItem(userKey)
            localStorage.removeItem(userDataKey)

            sessionStorage.removeItem(tokenKey)
            sessionStorage.removeItem(userKey)
            sessionStorage.removeItem(userDataKey)
        },

        /**
         * @param {{ avatar: string, nickname: string }} userData
         */
        setUserData(userData) {
            this.userData = userData
        }
    }
})
