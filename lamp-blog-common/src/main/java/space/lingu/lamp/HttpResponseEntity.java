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

package space.lingu.lamp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * @author RollW
 */
public class HttpResponseEntity<D> extends ResponseEntity<HttpResponseBody<D>> {
    public HttpResponseEntity(HttpStatus status) {
        super(status);
    }

    public HttpResponseEntity(HttpResponseBody<D> body) {
        super(body, null, body.getStatus());
    }

    public HttpResponseEntity(HttpResponseBody<D> body, MultiValueMap<String, String> headers) {
        super(body, headers, body.getStatus());
    }

    public HttpResponseEntity<D> fork() {
        return new HttpResponseEntity<>(getBody(), getHeaders());
    }

    public HttpResponseEntity<D> fork(HttpResponseBody<D> newResponseBody) {
        return new HttpResponseEntity<>(newResponseBody, getHeaders());
    }

    public static <D> HttpResponseEntity<D> of(HttpResponseBody<D> body) {
        return new HttpResponseEntity<>(body);
    }

    public static <D> HttpResponseEntity<D> success() {
        return HttpResponseEntity.of(
                HttpResponseBody.success()
        );
    }

    public static <D> HttpResponseEntity<D> success(String message) {
        return HttpResponseEntity.of(
                HttpResponseBody.success(message)
        );
    }

    public static <D> HttpResponseEntity<D> success(String message, D data) {
        return HttpResponseEntity.of(
                HttpResponseBody.success(message, data)
        );
    }

    public static <D> HttpResponseEntity<D> success(D data) {
        return HttpResponseEntity.of(
                HttpResponseBody.success(data)
        );
    }

    public static <D> HttpResponseEntity<D> of(ErrorCode errorCode,
                                               int status,
                                               String message,
                                               D data) {
        return HttpResponseEntity.of(
                HttpResponseBody.of(errorCode, status, message, data)
        );
    }

    public static <D> HttpResponseEntity<D> of(ErrorCode errorCode,
                                               String message) {
        return HttpResponseEntity.of(
                HttpResponseBody.of(errorCode, message)
        );
    }

    public static <D> HttpResponseEntity<D> of(ErrorCode errorCode) {
        return HttpResponseEntity.of(
                HttpResponseBody.of(errorCode)
        );
    }

    public static <D> HttpResponseEntity<D> of(ErrorCode errorCode,
                                               String message,
                                               D data) {
        return HttpResponseEntity.of(
                HttpResponseBody.of(errorCode, message, data)
        );
    }

    public static <D> HttpResponseEntity<D> of(ErrorCode errorCode,
                                               D data) {
        return HttpResponseEntity.of(
                HttpResponseBody.of(errorCode, data)
        );
    }

}
