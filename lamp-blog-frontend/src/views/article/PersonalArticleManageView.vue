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
  <div class="p-5">
    <n-h1>文章管理</n-h1>
    <div class="flex items-baseline mt-5">
      <n-button-group>
        <n-button>创建文章</n-button>
        <n-button>删除文章</n-button>
      </n-button-group>
      <div class="flex flex-grow justify-end">
        <n-input class="w-80" placeholder="搜索文章"/>
      </div>
    </div>
    <n-h2>文章列表</n-h2>
    <n-card v-for="article in articles" :key="article.id" class="mt-5">
      <template #header>
        <n-h3>{{ article.title }}</n-h3>
      </template>
      <template #default>
        <div class="w-full h-full">
          <MarkdownRender :value="article.content"/>
        </div>
      </template>
      <template #footer>
        <n-button-group>
          <n-button>编辑</n-button>
          <n-button>删除</n-button>
        </n-button-group>
      </template>
    </n-card>

  </div>
</template>

<script setup>
import {ref} from "vue";
import axios from "axios";
import api from "@/request/api";
import {useRouter} from "vue-router";
import {createConfig} from "@/request/axios_config";
import MarkdownRender from "@/components/markdown/MarkdownRender.vue";
import {useUserStore} from "@/stores/user";

// TODO: user articles list

const articles = ref([]);
const router = useRouter();

const userStore = useUserStore();
const userId = userStore.getUser.id;

const pageRef = ref(1)

const requestArticles = (page) => {
  const config = createConfig()
  config.params = {
    page: page,
  }
  axios.get(api.userArticles(userId, false), config).then((res) => {
    console.log(res)
    res.data.data.forEach((article) => {
      article.content = article.content.split('\n').slice(0, 5).join('\n') + "\n\n......"
    })
    articles.value = res.data.data;
  }).catch((err) => {
    console.log(err)
  })
}

requestArticles(pageRef.value)

</script>
