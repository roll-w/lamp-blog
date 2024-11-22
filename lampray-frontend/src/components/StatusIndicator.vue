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

<script setup>
const props = defineProps({
    status: {
        type: String,
        default: "success"
    },
    title: {
        type: String,
        default: ""
    },
    messages: {
        type: Array,
        default: () => []
    }
})

const getIndicatorClass = () => {
    if (props.status === "error") {
        return "bg-red-600"
    }
    if (props.status === "warning") {
        return "bg-amber-400"
    }
    return "bg-green-600"
}

</script>

<template>
    <div class="py-2">
        <div class="flex items-center">
            <div :class="['w-4 h-4 rounded-full animate-ping opacity-75 ', getIndicatorClass()]"></div>
            <div :class="['w-4 h-4 rounded-full absolute', getIndicatorClass()]"></div>
            <div class="ml-3 text-gray-500">
                {{ title }}
            </div>
        </div>
        <div v-if="messages && messages.length" class="pt-1 ml-7 ">
            <div v-for="message in messages">
                - {{ message }}
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>