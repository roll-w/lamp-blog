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
    <div class="flex flex-col w-100 p-5 items-center">
        <n-card class="p-5 w-75 justify-center justify-self-center">
            <div class="flex place-items-stretch">
                <n-h1>文章编辑</n-h1>
                <div class="flex flex-grow justify-end">
                    <n-button type="primary" @click="onPublishClick">发布</n-button>
                </div>
            </div>
            <n-form ref="form" :model="formValue" :rules="formRules">
                <n-form-item label="文章标题">
                    <n-input v-model:value="formValue.title" :maxlength="100" clearable
                             placeholder="请输入标题"
                             show-count type="text"/>
                </n-form-item>
                <div class="flex w-100 items-stretch">
                    <div class="w-[50%] min-w-[50%]">
                        <n-form-item label="文章写作">
                            <n-input
                                    v-model:value="formValue.content"
                                    :autosize="{
                minRows: 10,
              }"
                                    class="w-100"
                                    placeholder="文章写作"
                                    type="textarea"
                            />
                        </n-form-item>
                    </div>
                    <div class="ml-5 flex-grow ">
                        <n-form-item class="w-100 h-100" label="文章预览">
                            <n-card class="mt-3 w-100 h-100" embedded>
                                <MarkdownRender :value="formValue.content"/>
                            </n-card>
                        </n-form-item>
                    </div>
                </div>
            </n-form>
        </n-card>
    </div>

</template>

<script setup>

import {onBeforeMount, onBeforeUnmount, ref, getCurrentInstance} from "vue";
import {useRouter} from "vue-router";
import {useNotification, useMessage, useDialog} from "naive-ui";
import MarkdownRender from "@/components/markdown/MarkdownRender.vue";
import {onBeforeRouteLeave} from "vue-router";
import api from "@/request/api";
import {createConfig} from "@/request/axios_config";
import {popUserErrorTemplate} from "@/views/utils/error";

const router = useRouter()
const {proxy} = getCurrentInstance()
const notification = useNotification()
const message = useMessage()
const dialog = useDialog()

const form = ref(null)

const formValue = ref({
    title: null,
    content: null
})

const formRules = {
    title: [{
        required: true,
        validator(rule, value) {
            if (value.length >= 100) {
                return new Error("标题长度不能超过100个字符")
            }
            return true
        },
        trigger: ['input']
    }],
    content: [{
        required: true,
        message: "文章内容不能为空",
        trigger: ['input']
    }],
}


onBeforeMount(() => {
    window.onbeforeunload = (e) => {
        return '系统可能不会保存您所做的更改。'
    }
})

onBeforeUnmount(() => {
    window.onbeforeunload = null
})

onBeforeRouteLeave((to, from, next) => {
    dialog.warning({
        title: '是否离开',
        content: '系统可能不会保存您所做的更改。',
        positiveText: '确认',
        negativeText: '取消',
        onPositiveClick: () => {
            next()
        },
        onNegativeClick: () => {
        }
    })
})

const validateFormValue = (callback) => {
    form.value?.validate((errors) => {
        if (errors) {
            return
        }
        callback()
    });
}

const onPublishSuccess = () => {
    notification.success({
        title: "发布成功",
        content: "文章发布成功，请前往文章列表查看。",
        meta: "文章发布成功",
        duration: 3000,
        keepAliveOnHover: true
    })
    window.onbeforeunload = null
    onBeforeRouteLeave((to, from, next) => {
        next()
    })
}

const publishContent = () => {
    const config = createConfig(true)
    proxy.$axios.post(api.articles(false),
            formValue.value, config)
            .then(res => {
                console.log(res)
                onPublishSuccess()
            })
            .catch(err => {
               popUserErrorTemplate(notification, err,
                       "文章发布失败", "文章请求错误")
            })
}

const onPublishClick = () => {
    dialog.warning({
        title: '是否发布',
        content: '请再次确认发布内容。',
        positiveText: '确认',
        negativeText: '取消',
        onPositiveClick: () => {
            validateFormValue(publishContent)
        },
        onNegativeClick: () => {
        }
    })
}

</script>
