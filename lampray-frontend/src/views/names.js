/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

export const getFileType = (type = 'OTHER') => {
    switch (type.toUpperCase()) {
        case 'IMAGE':
            return '图片'
        case 'VIDEO':
            return '视频'
        case 'AUDIO':
            return '音频'
        case 'DOCUMENT':
            return '文档'
        case 'TEXT':
            return '文本'
        case 'COMPRESSED':
            return '压缩包'
        case 'FILE':
            return '文件'
        case 'FOLDER':
            return '文件夹'
        case 'LINK':
            return '链接'
        default:
            return '其他'
    }
}

export const toFileType = (type = '其他') => {
    switch (type) {
        case '图片':
            return 'IMAGE'
        case '视频':
            return 'VIDEO'
        case '音频':
            return 'AUDIO'
        case '文档':
            return 'DOCUMENT'
        case '文本':
            return 'TEXT'
        case '压缩包':
            return 'COMPRESSED'
        case '文件':
            return 'FILE'
        case '文件夹':
            return 'FOLDER'
        case '链接':
            return 'LINK'
        default:
            return 'OTHER'
    }
}


export const getActionName = (action = 'UNKNOWN') => {
    switch (action.toUpperCase()) {
        case 'CREATE':
            return '创建'
        case 'UPDATE':
            return '更新'
        case 'DELETE':
            return '删除'
        case 'MOVE':
            return '移动'
        case 'COPY':
            return '复制'
        case 'RENAME':
            return '重命名'
        case 'ACCESS':
            return '访问'
        case 'EDIT':
            return '编辑'
        case 'UNKNOWN':
            return '未知'
    }
}

export const getSystemResourceKindName = (kind = 'UNKNOWN') => {
    switch (kind.toUpperCase()) {
        case 'FILE':
            return '文件'
        case 'FOLDER':
            return '文件夹'
        case 'LINK':
            return '链接'
        case 'TAG':
            return '标签'
        case 'TAG_GROUP':
            return '标签组'
        case 'USER':
        case 'USER_SETTING':
            return '用户'
        case 'GROUP':
        case 'GROUP_SETTING':
            return '用户组'
        case 'FAVORITE_GROUP':
            return '收藏夹'
        case 'FAVORITE_ITEM':
            return '收藏'
        default:
        case 'UNKNOWN':
            return '未知'
    }
}

export const getKeywordSearchScopeName = (name) => {
    if (!name) {
        return 'null'
    }
    switch (name.toUpperCase()) {
        case 'NAME':
            return '名称'
        case 'DESCRIPTION':
            return '描述'
        case 'CONTENT':
            return '内容'
        case 'ALL':
            return '所有'
    }
}

export const getRoleName = (name) => {
    if (!name) {
        return 'null'
    }
    switch (name.toUpperCase()) {
        case 'USER':
            return '用户'
        case 'ADMIN':
            return '管理员'
        case 'EDITOR':
            return '编辑'
        case 'UNASSIGNED':
            return '未分配'
        case 'CUSTOMER_SERVICE':
            return '客服'
        case 'REVIEWER':
            return '审核员'
    }
}

export const getJobStatusName = (name) => {
    if (!name) {
        return 'null'
    }

    switch (name.toUpperCase()) {
        case 'NOT_STARTED':
            return '未开始'
        case 'RUNNING':
            return '运行中'
        case 'PAUSED':
            return '暂停'
        case 'FINISHED':
            return '完成'
        case 'FAILED':
            return '失败'
        case 'CANCELED':
            return '取消'
        case 'UNKNOWN':
            return '未知'
    }
}
