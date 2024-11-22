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

package tech.lamprism.lampray.web.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
public final class RequestUtils {

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "X-Real-IP",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private static boolean isInvalidIp(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    public static String getRemoteIpAddress(HttpServletRequest request) {
        String ip = null;
        String ipAddresses = null;
        for (String header : HEADERS_TO_TRY) {
            ipAddresses = request.getHeader(header);
            if (!isInvalidIp(ipAddresses)) {
                break;
            }
        }
        if (!isInvalidIp(ipAddresses)) {
            ip = ipAddresses.split(",")[0];
        }
        if (isInvalidIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private RequestUtils() {
    }
}
