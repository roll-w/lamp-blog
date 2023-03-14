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
    <AdminBreadcrumb :location="reviewsQueue" :menu="menuReview"/>
    <n-h1>审核队列</n-h1>
    <n-text class="mt-5">
      内容审核队列
    </n-text>

    <n-card v-if="data.length" class="mt-5">
      <div class="text-xl">标题：
        <n-text code>{{ content.title }}</n-text>
      </div>
      <div class="mt-2">类型：{{ contentTypeTransform(content.type) }}</div>
      <div class="mt-2">审核标记：{{ markTransform(data[curIndex].reviewMark) }}</div>
      <div class="mt-2">审核内容：</div>
      <n-card class="mt-4" embedded>
        <MarkdownRender :value="content.content"/>
      </n-card>
      <template #footer>
        <n-button-group class="mt-5">
          <n-button secondary type="error" @click="showModal = true">拒绝</n-button>
          <n-popconfirm
              @positive-click="onPassConfirm">
            <template #trigger>
              <n-button secondary type="success" @click="">通过</n-button>
            </template>
            是否确认通过
          </n-popconfirm>
        </n-button-group>
      </template>
    </n-card>
    <n-button-group v-if="data.length" class="mt-5">
      <n-button @click="prev">上一篇</n-button>
      <n-button @click="next">下一篇</n-button>
    </n-button-group>
    <div v-else class="mt-5 text-2xl">
      当前暂无待审核内容
    </div>
    <n-modal v-model:show="showModal" preset="dialog" title="确认">
      <template #default>
        <n-form-item label="拒绝理由">
          <n-input v-model:value="reason" :autosize="{minRows: 3}" placeholder="请输入拒绝理由"
                   type="textarea"/>
        </n-form-item>
      </template>
      <template #action>
        <n-button-group>
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" @click="onRejectConfirm">确认</n-button>
        </n-button-group>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import AdminBreadcrumb from "@/components/admin/AdminBreadcrumb.vue";
import {menuReview} from "@/views/menu";
import {reviewsQueue} from "@/router";
import {createConfig} from "@/request/axios_config";
import axios from "axios";
import api from "@/request/api";
import {formatTimestamp} from "@/util/time";
import {getCurrentInstance, ref, watch} from "vue";
import {useNotification} from "naive-ui";
import MarkdownRender from "@/components/markdown/MarkdownRender.vue";
import {adminErrorTemplate} from "@/views/admin/error";

const {proxy} = getCurrentInstance()
const notification = useNotification()

const showModal = ref(false)

const data = ref([])
const content = ref({})
const curIndex = ref(0)

const reason = ref(null)

const page = ref(1)

const prev = () => {
  if (curIndex.value > 0) {
    curIndex.value--
    return
  }
  notification.warning({
    title: "已经是第一篇了",
    content: "没有上一篇了"
  })
}

const next = () => {
  if (curIndex.value < data.value.length - 1) {
    curIndex.value++
    return
  }
  notification.warning({
    title: "已经是最后一篇了",
    content: "没有下一篇了"
  })
}

watch(curIndex, (newVal) => {
  const cur = data.value[newVal]
  if (cur) {
    requestContent(cur.id)
  }
})

const markTransform = (reviewMark) => {
  switch (reviewMark) {
    case "NORMAL":
      return "常规"
    case "REPORT":
      return "举报"
    default:
      return reviewMark
  }
}

const statusTransform = (reviewStatus) => {
  switch (reviewStatus) {
    case "NOT_REVIEWED":
      return "未审核"
    case "REVIEWED":
      return "已通过"
    case "REJECTED":
      return "已拒绝"
    default:
      return reviewStatus
  }
}

const contentTypeTransform = (contentType) => {
  switch (contentType) {
    case "ARTICLE":
      return "文章"
    case "COMMENT":
      return "评论"
    case "POST":
      return "动态"
    default:
      return contentType
  }
}

const requestForData = (page, size) => {
  const config = createConfig()
  config.params = {
    page: page,
    size: size,
    status: "UNFINISHED"
  }
  proxy.$axios.get(api.currentReviews, config).then((res) => {
    const recvData = res.data
    recvData.forEach((item) => {
      if (item.assignedTime)
        item.assignedTime = formatTimestamp(item.assignedTime)
      else item.assignedTime = "未分配"
      if (item.reviewTime)
        item.reviewTime = formatTimestamp(item.reviewTime)
      else item.reviewTime = "未审核"
    })
    data.value = recvData
    if (recvData.length > 0) {
      requestContent(recvData[0].id || undefined)
    }
  }).catch((err) => {
    adminErrorTemplate(notification, err,
        "请求错误", "审核任务请求错误")
  })
}

const requestContent = (id) => {
  if (id === undefined) {
    return
  }

  const config = createConfig()
  proxy.$axios.get(api.reviewContent(id), config).then((res) => {
    content.value = res.data
  }).catch((err) => {
    console.log(err)
    adminErrorTemplate(notification, err,
        "请求错误", "内容请求错误")
  })
}

const passReview = (id, callback) => {
  const config = createConfig()
  proxy.$axios.put(api.reviewResource(id), {}, config).then((res) => {
    notification.success({
      title: "审核通过",
      content: "审核成功通过",
      meta: "请求成功",
      duration: 3000,
      keepAliveOnHover: true
    })
    callback()
  }).catch((err) => {
    console.log(err)
    adminErrorTemplate(notification, err,
        "审核请求错误", "审核通过请求错误")
  })
}

const rejectReview = (id, callback) => {
  const config = createConfig(true)
  const data = {
    reason: reason.value
  }
  axios.patch(api.reviewResource(id), data, config).then((res) => {
    console.log(res)
    reason.value = null
    notification.success({
      title: "审核拒绝",
      content: "审核成功拒绝",
      meta: "请求成功",
      duration: 3000,
      keepAliveOnHover: true
    })
    callback()
  }).catch((err) => {
    console.log(err)
    adminErrorTemplate(notification, err,
        "审核请求错误", "审核拒绝请求错误")
    reason.value = null
  })
}

const onRejectConfirm = () => {
  if (reason.value === null || reason.value === "") {
    notification.warning({
      title: "拒绝原因不能为空",
      content: "请填写拒绝原因",
      meta: "拒绝原因为空",
      duration: 3000,
      keepAliveOnHover: true
    })
    return
  }
  showModal.value = false
  rejectReview(data.value[curIndex.value].id, () => {
    next()
  })
}

const onPassConfirm = () => {
  passReview(data.value[curIndex.value].id, () => {
    next()
  })
}

requestForData(page.value, 10)

</script>
