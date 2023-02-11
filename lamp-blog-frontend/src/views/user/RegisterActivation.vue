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
  <div class="flex items-center justify-center">
    <div class="flex-col p-5 items-center justify-items-center">
      <n-h1>
        即将激活你的账户，请确认你的账户名
      </n-h1>

      <n-space vertical size="medium">
        <n-text>访问账号的最后一步。</n-text>
        <n-card embedded>激活确认码：<code>{{ token }}</code></n-card>
        <n-divider />
        <div class="flex items-center justify-center">
          <n-button class="w-100">确认激活</n-button>
        </div>
      </n-space>
    </div>
  </div>

</template>

<script setup>

import { useRouter } from "vue-router";

const router = useRouter()

const token = router.currentRoute.value.params.token

</script>
