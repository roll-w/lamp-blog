<!--
  - Copyright (C) 2023 RollW
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -        http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<script setup>
import {RouterView} from 'vue-router'
import BlogNavBar from "@/components/BlogNavBar.vue";

// highlight.js
import hljs from 'highlight.js/lib/core'
import cpp from 'highlight.js/lib/languages/cpp'
import css from 'highlight.js/lib/languages/css'
import java from 'highlight.js/lib/languages/java'
import html from 'highlight.js/lib/languages/xml'
import less from 'highlight.js/lib/languages/less'
import markdown from 'highlight.js/lib/languages/markdown'
import php from 'highlight.js/lib/languages/php'
import python from 'highlight.js/lib/languages/python'
import scss from 'highlight.js/lib/languages/scss'
import sql from 'highlight.js/lib/languages/sql'
import typescript from 'highlight.js/lib/languages/typescript'
import xml from 'highlight.js/lib/languages/xml'
import yaml from 'highlight.js/lib/languages/yaml'
import bash from 'highlight.js/lib/languages/bash'
import csharp from 'highlight.js/lib/languages/csharp'
import go from 'highlight.js/lib/languages/go'
import ini from 'highlight.js/lib/languages/ini'
import javascript from 'highlight.js/lib/languages/javascript'
import json from 'highlight.js/lib/languages/json'

hljs.registerLanguage('cpp', cpp)
hljs.registerLanguage('css', css)
hljs.registerLanguage('java', java)
hljs.registerLanguage('html', html)
hljs.registerLanguage('less', less)
hljs.registerLanguage('markdown', markdown)
hljs.registerLanguage('php', php)
hljs.registerLanguage('python', python)
hljs.registerLanguage('scss', scss)
hljs.registerLanguage('sql', sql)
hljs.registerLanguage('typescript', typescript)
hljs.registerLanguage('xml', xml)
hljs.registerLanguage('yaml', yaml)
hljs.registerLanguage('bash', bash)
hljs.registerLanguage('csharp', csharp)
hljs.registerLanguage('go', go)
hljs.registerLanguage('ini', ini)
hljs.registerLanguage('javascript', javascript)
hljs.registerLanguage('json', javascript)
// highlight.js

import {tokenKey, userKey, useUserStore} from "@/stores/user";
import router, {index} from "@/router";

const userStore = useUserStore()

router.beforeEach((to, from, next) => {
  if (!to.name.startsWith("admin")) {
    return next()
  }
  const role = userStore.user.role
  if (!userStore.isLogin || !role || role.value === "USER") {
    return next({
      name: index
    })
  }
  return next()
})


const loadFromLocal = () => {
  const token = localStorage.getItem("L2w9t0k3n")
  const user = JSON.parse(localStorage.getItem("user"))
  return {user, token}
}

const loadFromSession = () => {
  const token = sessionStorage.getItem("L2w9t0k3n")
  const user = JSON.parse(sessionStorage.getItem("user"))
  return {user, token}
}

const local = loadFromLocal()
const session = loadFromSession()

const tryLoginFromState = () => {
  if (local.user && local.token) {
    userStore.loginUser(local.user, local.token, true)
    return
  }
  if (session.user && session.token) {
    userStore.loginUser(session.user, session.token, false)
  }
}
tryLoginFromState()

userStore.$subscribe((mutation, state) => {
  console.log("subscribe value")
  if (!state.remember) {
    sessionStorage.setItem(tokenKey, state.token)
    sessionStorage.setItem(userKey, JSON.stringify(state.user))
    return
  }
  localStorage.setItem(tokenKey, state.token)
  localStorage.setItem(userKey, JSON.stringify(state.user))
})



</script>

<template>
  <n-config-provider :hljs="hljs" class="h-100">
    <n-loading-bar-provider>
      <n-message-provider>
        <n-notification-provider>
          <n-dialog-provider>
            <n-layout position="absolute">
              <BlogNavBar/>
              <router-view/>
            </n-layout>
          </n-dialog-provider>
        </n-notification-provider>
      </n-message-provider>
    </n-loading-bar-provider>
  </n-config-provider>
</template>

<style scoped>

</style>
