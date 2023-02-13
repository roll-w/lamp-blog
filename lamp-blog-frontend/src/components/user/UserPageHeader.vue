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
  <div :style="{'background-image': 'url(' + userInfo.cover + ')'}"
       class="rounded-2xl bg-no-repeat bg-cover bg-center">
    <div class="rounded-2xl p-7 pt-10 backdrop-brightness-75 ">
      <div class="flex items-center mt-10 w-100">
        <n-avatar :round="true" :size="80"
                  :src="userInfo.avatar"
                  class="mr-5"/>
        <div class="leading-loose">
          <div class="leading-tight">
            <div class="text-white text-2xl font-bold">
              {{ userInfo.nickname }}
            </div>
            <div class="text-gray-300 text-xl font-light">
              @{{ userInfo.username }}
            </div>
          </div>
          <div class="text-gray-300">
            {{ userInfo.introduction }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

const userInfo = defineProps({
  avatar: {
    type: String,
    required: true
  },
  nickname: {
    type: String,
    required: true
  },
  username: {
    type: String,
    required: true
  },
  introduction: {
    type: String,
    required: true
  },
  cover: {
    type: String,
    required: true
  }

})

</script>
