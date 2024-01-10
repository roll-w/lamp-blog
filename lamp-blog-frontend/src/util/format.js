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

export function formatTimestamp(timestamp, zero = '从未') {
    if (timestamp === 0) {
        return zero
    }
    let date = new Date(timestamp)
    let Y = date.getFullYear() + '-'
    let M = (date.getMonth() + 1 < 10
        ? '0' + (date.getMonth() + 1)
        : date.getMonth() + 1) + '-'
    let D = (date.getDate() < 10
        ? '0' + date.getDate()
        : date.getDate()) + ' '
    let h = date.getHours() + ':'
    let mm = date.getMinutes() < 10
        ? '0' + date.getMinutes()
        : date.getMinutes()
    let m = mm + ':'
    let s = date.getSeconds() < 10
        ? '0' + date.getSeconds()
        : date.getSeconds()
    return Y + M + D + h + m + s
}

export const formatTimestampDash = (timestamp, zero = '从未') => {
    if (timestamp === 0) {
        return zero
    }
    let date = new Date(timestamp)
    let Y = date.getFullYear() + '-'
    let M = (date.getMonth() + 1 < 10
        ? '0' + (date.getMonth() + 1)
        : date.getMonth() + 1) + '-'
    let D = (date.getDate() < 10
        ? '0' + date.getDate()
        : date.getDate()) + '-'
    let h = date.getHours() + '-'
    let mm = date.getMinutes() < 10
        ? '0' + date.getMinutes()
        : date.getMinutes()
    let m = mm + '-'
    let s = date.getSeconds() < 10
        ? '0' + date.getSeconds()
        : date.getSeconds()
    return Y + M + D + h + m + s
}

const gb = 1024 * 1024 * 1024
const mb = 1024 * 1024
const kb = 1024

export const formatFileSize = (sizeInBytes, infinity = '∞') => {
    if (sizeInBytes === Infinity || sizeInBytes < 0) {
        return infinity
    }

    if (sizeInBytes < kb) {
        return sizeInBytes + ' B'
    }
    if (sizeInBytes < mb) {
        return (sizeInBytes / kb).toFixed(2) + ' KB'
    }
    if (sizeInBytes < gb) {
        return (sizeInBytes / mb).toFixed(2) + ' MB'
    }
    return (sizeInBytes / gb).toFixed(2) + ' GB'
}

export const UNIT_MILLISECOND = 'millisecond'
export const UNIT_SECOND = 'second'
export const UNIT_MINUTE = 'minute'


export const formatDuration = (time, inUnit = UNIT_SECOND) => {
    switch (inUnit) {
        case UNIT_MILLISECOND:
            time = time / 1000
            break
        case UNIT_MINUTE:
            time = time * 60
            break
        default:
            break
    }
    const days = Math.floor(time / (3600 * 24));
    time -= days * 3600 * 24;
    const hours = Math.floor(time / 3600);
    time -= hours * 3600;
    const minutes = Math.floor(time / 60);
    time -= minutes * 60;
    const parts = [];
    if (days > 0) {
        parts.push(`${days} 天`);
    }
    if (hours > 0) {
        parts.push(`${hours} 小时`);
    }
    if (days < 1 && minutes > 0) {
        parts.push(`${minutes} 分`);
    }
    if (days < 1 && time > 0) {
        parts.push(`${time} 秒`);
    }
    if (parts.length === 0) {
        return '0 秒';
    }
    if (parts.length === 1) {
        return parts[0];
    }
    // const last = parts.pop();
    // return `${parts.join(', ')} and ${last}`;
    return `${parts.join(' ')}`;
}
