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
    <AdminBreadcrumb :location="adminUsers" :menu="menuUsers" />
    <n-h1>用户列表</n-h1>
    <n-data-table
        :bordered="false"
        :columns="columns"
        :data="data"
        :pagination="false"
    />
  </div>
</template>

<script setup>
import {h, ref} from "vue";
import {NButton, NButtonGroup} from "naive-ui";
import axios from "axios";
import api from "@/request/api";
import {useUserStore} from "@/stores/user";
import AdminBreadcrumb from "@/components/admin/AdminBreadcrumb.vue";
import {formatTimestamp} from "@/util/time";
import {adminUsers} from "@/router";
import {menuUsers} from "@/views/menu";

const userStore = useUserStore()

const columns = [
  {
    title: "用户ID",
    key: "userId"
  },
  {
    title: "用户名",
    key: "username"
  },
  {
    title: "昵称",
    key: "nickname"
  },
  {
    title: "角色",
    key: "role"
  },
  {
    title: "电子邮箱",
    key: "email"
  },
  {
    title: "注册时间",
    key: "createdAt"
  },
  {
    title: "操作",
    key: "actions",
    render(row) {
      return h(
          NButtonGroup,
          {},
          () => [
            h(NButton,
                {
                  size: 'small',
                  onClick: (row) => {
                    console.log(row)
                  }
                },
                {default: () => "查看"}),
            h(NButton,
                {
                  size: 'small',
                  onClick: () => {
                    console.log("lcl")
                  }
                },
                {default: () => "编辑"}),
            h(NButton,
                {
                  size: 'small',
                  onClick: () => {
                    console.log("lcl")
                  }
                },
                {default: () => "删除"}),
          ]
      );
    }
  }
]

const data = ref([]);

const requestForData = (page, size) => {
  axios.get(api.userList, {
    params: {
      page: page,
      size: size
    },
    headers: {
      "Authorization": userStore.getToken
    }
  }).then((res) => {
    const recvData = res.data.data
    recvData.forEach((item) => {
      item.createdAt = formatTimestamp(item.createdAt)
    })
    data.value = recvData
    console.log(res)
  }).catch((err) => {
    console.log(err)
  })
}

requestForData(1, 10)

</script>

<style scoped>

</style>