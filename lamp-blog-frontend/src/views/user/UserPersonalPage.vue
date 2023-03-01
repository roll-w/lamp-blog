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
  <n-card :bordered="false" size="large">
    <UserPageHeader v-if="userInfo"
                    :avatar="userInfo.avatar"
                    :introduction="userInfo.introduction"
                    :nickname="userInfo.nickname"
                    :username="userInfo.username"
                    cover="https://naive-ui.oss-cn-beijing.aliyuncs.com/carousel-img/carousel1.jpeg"
    />

    <div v-else class="text-3xl p-10">
      {{ message }}
    </div>
  </n-card>

</template>

<script setup>
import {useRouter} from "vue-router";
import {ref} from "vue";
import axios from "axios";
import api from "@/request/api";
import {createConfig} from "@/request/axios_config";
import UserPageHeader from "@/components/user/UserPageHeader.vue";

const router = useRouter()

const id = router.currentRoute.value.params.userId

const userInfo = ref(null)
const message = ref(null)


const getMessage = (status) => {
  if (status >= 500) {
    return "请求信息错误"
  }
  if (status === 404) {
    return "用户不存在"
  }
  return "用户注销或被屏蔽"
}

const getUserInfo = () => {
  const config = createConfig()
  axios.get(api.userInfo(id, false), config)
      .then(res => {
        const userData = res.data.data
        if (userData.introduction === null) {
          userData.introduction = "这个人很懒，什么都没留下"
        }

        userInfo.value = res.data.data
        console.log(res)
      })
      .catch(err => {
        const resp = err.response.data
        message.value = getMessage(resp.status)
      })
}

getUserInfo()

</script>

<style scoped>

</style>