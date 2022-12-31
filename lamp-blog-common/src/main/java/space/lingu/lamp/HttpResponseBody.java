/*
 * Copyright (C) 2022 Lingu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.lamp;


/**
 * @author RollW
 */
@SuppressWarnings("unchecked")
public class HttpResponseBody<D> {
    private static final HttpResponseBody<?> SUCCESS = new HttpResponseBody<>(
            CommonErrorCode.SUCCESS, 200, "OK");

    private final int status;
    private String message;
    private ErrorCode errorCode;

    private D data;

    private HttpResponseBody() {
        this.status = 200;
    }

    public HttpResponseBody(ErrorCode errorCode, int status, String message) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    public HttpResponseBody(ErrorCode errorCode, int status, String message,
                            D data) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    private HttpResponseBody<D> setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    private HttpResponseBody<D> setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public D getData() {
        return data;
    }

    private HttpResponseBody<D> setData(D data) {
        this.data = data;
        return this;
    }

    public HttpResponseBody<D> fork() {
        return new HttpResponseBody<>(errorCode, status, message, data);
    }

    public static <D> HttpResponseBody<D> success() {
        return (HttpResponseBody<D>) SUCCESS;
    }

    public static <D> HttpResponseBody<D> success(String message) {
        return (HttpResponseBody<D>) success()
                .fork()
                .setMessage(message);
    }

    public static <D> HttpResponseBody<D> success(String message, D data) {
        return (HttpResponseBody<D>) success()
                .fork()
                .setMessage(message)
                .setData(data);
    }

    public static <D> HttpResponseBody<D> success(D data) {
        return (HttpResponseBody<D>) success()
                .fork()
                .setData(data);
    }

    // for semantic control

    public static <D> HttpResponseBody<D> failure(ErrorCode errorCode,
                                                  int status,
                                                  String message,
                                                  D data) {
        return new HttpResponseBody<>(errorCode, status, message, data);
    }

    public static <D> HttpResponseBody<D> failure(ErrorCode errorCode,
                                                  int status,
                                                  String message) {

        return new HttpResponseBody<>(errorCode, status, message);
    }

    public static <D> HttpResponseBody<D> failure(ErrorCode errorCode,
                                                  String message) {
        return new HttpResponseBody<>(errorCode, errorCode.getStatus(), message);
    }

    public static <D> HttpResponseBody<D> failure(ErrorCode errorCode,
                                                  String message,
                                                  D data) {
        return new HttpResponseBody<>(errorCode, errorCode.getStatus(), message, data);
    }
}
