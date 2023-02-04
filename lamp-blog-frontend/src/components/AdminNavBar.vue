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
  <!--  TODO: collapse sidebar -->
  <n-layout-sider :collapsed-width="0"
                  :native-scrollbar="false"
                  bordered
                  position="absolute"
                  width="var(--sidebar-width)">
    <div>
      <n-menu :options="menuOptions" :value="chooseOn"/>
    </div>
  </n-layout-sider>
</template>

<script setup>

import {RouterLink, useRouter} from "vue-router";
import {h} from "vue";

const router = useRouter()

const routerName = router.currentRoute.value.name

const calcChooseOption = () => {
  switch (routerName) {
    case "admin-index":
      return "home"
    case "index":
      return "blog"
    default:
      return "home"
  }
}
const chooseOn = calcChooseOption()

const menuOptions = [
  {
    label: () => h(
        RouterLink,
        {
          to: {
            name: "admin-index",
          }
        },
        {default: () => "系统首页"}
    ),
    key: "home",
  },
  {
    label: () => h(
        RouterLink,
        {
          to: {
            name: "index",
          }
        },
        {default: () => "返回博客"}
    ),
    key: "blog",

  },

  {
    label: "用户管理",
    key: "user-management",
    children: [
      {
        label: "用户列表",
        key: "user-management-list"
      },
      {
        label: "工作人员管理",
        key: "user-management-staff"
      }
    ]
  },
  {
    label: "文章管理",
    key: "article-management",
    children: [
      {
        label: "文章列表",
        key: "article-management-list"
      },
      {
        label: "文章分类",
        key: "article-management-category"
      },
      {
        label: "文章标签",
        key: "article-management-tag"
      },
    ]
  },
  {
    label: "评论管理",
    key: "comment-management",
    children: [
      {
        label: "评论列表",
        key: "comment-management-list"
      },
    ]
  },
  {
    label: "审核管理",
    key: "audit-management",
    children: [
      {
        label: "审核列表",
        key: "audit-management-list"
      },
      {
        label: "审核任务",
        key: "audit-management-task"
      }
    ]
  },
  {
    label: "系统管理",
    key: "system-management",
    children: [
      {
        label: "系统设置",
        key: "system-management-setting"
      },
      {
        label: "系统日志",
        key: "system-management-log"
      },
      {
        label: "系统监控",
        key: "system-management-monitor"
      },
    ]
  },
]

</script>

<style scoped>

</style>