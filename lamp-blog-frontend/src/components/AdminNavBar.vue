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
<!--  <n-watermark-->
<!--      :content="username  + ' ID:' + userId + ' ' + role"-->
<!--      :font-size="16"-->
<!--      :height="384"-->
<!--      :line-height="16"-->
<!--      :rotate="-15"-->
<!--      :width="384"-->
<!--      :x-offset="12"-->
<!--      :y-offset="60"-->
<!--      cross-->
<!--      fullscreen-->
<!--  />-->
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
import {h, onBeforeMount, ref} from "vue";
import {admin, adminUsers, index, systemLog} from "@/router";
import {useUserStore} from "@/stores/user";
import {useMessage} from "naive-ui";

const message = useMessage()
const userStore = useUserStore()
const username = ref(userStore.user.username)
const userId = ref(userStore.user.id)
const role = ref(userStore.user.role)

const router = useRouter()
const checkAdminRole = () => {
  if (!userStore.isLogin || !role || role.value === "USER") {
    message.error("无权限访问该页面")
    router.push({name: index})
    return false
  }
  return true
}

onBeforeMount(() => {
  checkAdminRole()
})

let routerName = router.currentRoute.value.name
const chooseOn = ref()
const calcChooseOption = () => {
  return routerName
}

chooseOn.value = calcChooseOption()

router.afterEach(() => {
  routerName = router.currentRoute.value.name
  chooseOn.value = calcChooseOption()
})



const menuOptions = [
  {
    label: () => h(
        RouterLink,
        {
          to: {
            name: admin,
          }
        },
        {default: () => "系统首页"}
    ),
    key: admin,
  },
  {
    label: () => h(
        RouterLink,
        {
          to: {
            name: index,
          }
        },
        {default: () => "返回博客"}
    ),
    key: index,

  },
  {
    label: "用户管理",
    key: "user-management",
    children: [
      {
        label: () => h(
            RouterLink,
            {
              to: {
                name: adminUsers,
              }
            },
            {default: () => "用户列表"}
        ),
        key: adminUsers
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
        label: () => h(
            RouterLink,
            {
              to: {
                name: systemLog,
              }
            },
            {default: () => "系统日志"}
        ),
        key: systemLog
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