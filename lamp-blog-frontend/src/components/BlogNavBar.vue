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
  <n-layout-header bordered style="height: var(--header-height)">
    <div class="p-5 flex">
      <n-text :depth="1" class="ui-logo flex justify-start" @click="handleLogoClick">
        <span>Lamp Blog</span>
      </n-text>
      <div class="flex justify-end justify-items-end flex-grow">
        <n-button v-if="!userStore.isLogin" @click="handleLoginClick">登录</n-button>
        <n-dropdown v-else :options="options" trigger="hover" @select="handleSelect">
          <n-avatar>{{ username }}</n-avatar>
        </n-dropdown>
      </div>
    </div>
  </n-layout-header>
</template>


<script setup>
import {useRouter} from "vue-router";
import {useUserStore} from "@/stores/user";
import {h, ref} from "vue";
import {NAvatar, NText} from "naive-ui";

const router = useRouter();
const userStore = useUserStore();

const username = ref(userStore.user.username);
const role = ref(userStore.user.role);

const userOptions = [
  {
    label: `${userStore.user.username}`,
    key: "username",
  },
  {
    label: "文章管理",
    key: "article",
  },
  {
    label: "个人中心",
    key: "center",
  },
  {
    key: 'header-divider',
    type: 'divider'
  },
  {
    label: "退出",
    key: "logout",
  }
]

const adminOptions = [
  {
    label: "个人中心",
    key: "center",
  },
  {
    label: "文章管理",
    key: "article",
  },
  {
    label: "系统管理",
    key: "system",
  },
  {
    key: 'header-divider',
    type: 'divider'
  },
  {
    label: "退出",
    key: "logout",
  }
]

const options = ref(userOptions)

const chooseOptions = () => {
  if (!role.value) {
    return
  }
  if (role.value !== "USER") {
    options.value = adminOptions
  } else {
    options.value = userOptions
  }
}

router.afterEach(() => {
  username.value = userStore.user.username;
  role.value = userStore.user.role;
  chooseOptions()
})

const handleLogoClick = () => {
  router.push({
    name: "index"
  });
};

const handleLoginClick = () => {
  router.push({
    name: "login-page"
  });
};

const handleSelect = (key) => {
  switch (key) {
    case "logout":
      userStore.logout();
      router.push({
        name: "index"
      })
      break;
    case "system":
      router.push({
        name: "admin-index"
      })
      break;
  }
}


</script>

<style scoped>
.nav {
  display: grid;
  align-items: center;
}

.ui-logo {
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 18px;
}

.ui-logo > img {
  margin-right: 12px;
  height: 32px;
  width: 32px;
}

.nav-menu {
  padding-left: 36px;
}

.nav-picker {
  margin-right: 4px;
}

.nav-picker.padded {
  padding: 0 10px;
}

.nav-picker:last-child {
  margin-right: 0;
}

.nav-end {
  display: flex;
  align-items: center;
}
</style>

<style>
</style>
