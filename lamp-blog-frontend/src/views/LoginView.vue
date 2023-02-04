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
  <div class="center">
    <n-grid :cols="2" class="pr-10 pl-10">
      <n-gi>
        <n-card>
          <n-h1>
            <n-text type="primary">Lamp Blog</n-text>
          </n-h1>
          <n-p>Write blogs in <b>Lamp Blog.</b></n-p>
          <n-card :bordered="false" embedded>
            <n-code :code="code" language="java" show-line-numbers/>
          </n-card>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card>
          <LoginForm v-if="isLoginPage()"/>
          <RegisterForm v-else/>
        </n-card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup>
import RegisterForm from "@/components/user/RegisterForm.vue";
import LoginForm from "@/components/user/LoginForm.vue";
import {useRouter} from "vue-router";

const router = useRouter()

const isLoginPage = () => {
  return router.currentRoute.value.name === 'login-page'
}

const code = `
public class LampBlog {
  public static void main(String[] args) {
    System.out.println("Hello, Lamp Blog!");
  }
}
`
</script>

<style scoped>
.center {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 64px);
  justify-content: center;
  position: relative;
}
</style>
