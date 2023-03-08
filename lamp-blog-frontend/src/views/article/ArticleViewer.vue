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

<template>
  <div class="p-10">
    <div>
      <div class="text-3xl font-bold">
        {{ article.title }}
      </div>
      <div class="mt-2 text-sm text-gray-400">
        发布时间：<span class="text-gray-400">{{ formatTimestamp(article.createTime) }}</span>
      </div>
    </div>
    <div class="flex flex-row mt-5 ">
      <div class="w-1/5 overflow-auto">
        <n-card>
          <n-avatar :round="true" :size="40"
                    :src="userInfo.avatar"
                    class="mr-5"/>
          <div class="leading-loose">
            <div class="leading-tight">
              <div class="text-gray-700 dark:text-white text-xl font-bold">
                <router-link #="{ navigate, href }"
                             :to="route"
                             custom>
                  <n-a :href="href" @click="navigate">
                    {{ userInfo.nickname }}
                  </n-a>
                </router-link>
              </div>
              <div class="text-gray-400 text-lg font-light">
                @{{ userInfo.username }}
              </div>
            </div>
            <div class="text-gray-400">
              {{ userInfo.introduction }}
            </div>
          </div>
        </n-card>
        <div class="sticky mt-3">
          <n-card>
            文章查看
            <!-- TODO: menu -->
          </n-card>
        </div>
      </div>
      <div class="flex-grow ml-5">
        <div v-if="article">
          <n-card>
            <MarkdownRender
                :value="article.content"/>
          </n-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

import {useNotification} from "naive-ui";
import {createConfig} from "@/request/axios_config";
import api from "@/request/api";
import axios from "axios";
import {getCurrentInstance, ref} from "vue";
import {useRouter} from "vue-router";
import MarkdownRender from "@/components/markdown/MarkdownRender.vue";
import {formatTimestamp} from "@/util/time";
import {getTitleSuffix, userPage} from "@/router";

const router = useRouter()
const {proxy} = getCurrentInstance()

const userInfo = ref({})
const notification = useNotification()

const article = ref({})

const userId = router.currentRoute.value.params.user
const articleId = router.currentRoute.value.params.id

const route = {
  name: userPage,
  params: {
    userId: -1
  }
}

const requestUserInfo = (id) => {
  proxy.$axios.get(api.userInfo(id, false), createConfig())
      .then((res) => {
        console.log(res)
        userInfo.value = res.data
        route.params.userId = userInfo.value.userId
      })
      .catch((err) => {
        console.log(err)
        notification.error({
          title: "请求错误",
          content: err.tip,
          meta: "作者请求错误",
          duration: 3000,
          keepAliveOnHover: true
        })
      })
}

const updateDocTitle = (title) => {
  document.title = title + "" + getTitleSuffix()
}


const requestArticle = (userId, articleId) => {
  proxy.$axios.get(api.articleDetails(userId, articleId, false), createConfig())
      .then((res) => {
        article.value = res.data
        updateDocTitle(article.value.title)
        requestUserInfo(article.value.authorId)
      })
      .catch((err) => {
        console.log(err)
        userInfo.value = {}
        notification.error({
          title: "请求错误",
          content: err.tip,
          meta: "文章请求错误",
          duration: 3000,
          keepAliveOnHover: true
        })
      })
}

requestArticle(userId, articleId)

</script>
