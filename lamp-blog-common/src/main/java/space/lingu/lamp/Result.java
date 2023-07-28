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

import space.lingu.NonNull;
import space.lingu.Nullable;
import tech.rollw.common.web.CommonErrorCode;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.ErrorCodeMessageProvider;
import tech.rollw.common.web.HttpResponseBody;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author RollW
 */
@Deprecated
public record Result<D>(
        @NonNull
        ErrorCode errorCode,

        @Nullable
        D data
) {
    public boolean failed() {
        return errorCode.failed();
    }

    public <R> Result<R> map(@NonNull Function<? super D, ? extends R> function) {
        if (!isPresent()) {
            return Result.of(errorCode, null);
        }
        return Result.of(errorCode, function.apply(data));
    }

    public <R> R transform(@NonNull Function<Result<? super D>, ? extends R> function) {
        return function.apply(this);
    }

    public <R> R extract(@NonNull Function<? super D, ? extends R> function) {
        return function.apply(data);
    }

    public HttpResponseBody<D> toResponseBody(String message) {
        if (errorCode.success()) {
            return HttpResponseBody.success(message, data());
        }
        return HttpResponseBody.of(errorCode(), message, data());
    }

    public HttpResponseBody<D> toResponseBody() {
        return toResponseBody((String) null);
    }

    public <T> HttpResponseBody<T> toResponseBody(String message, @NonNull Function<D, T> mapper) {
        if (errorCode.success()) {
            return HttpResponseBody.success(message, mapper.apply(data()));
        }
        return HttpResponseBody.of(errorCode(), message, mapper.apply(data()));
    }

    public <T> HttpResponseBody<T> toResponseBody(@NonNull ErrorCodeMessageProvider errorCodeMessageProvider,
                                                  @NonNull Function<D, T> mapper) {
        return toResponseBody(errorCodeMessageProvider.getMessage(errorCode), mapper);
    }

    public <T> HttpResponseBody<T> toResponseBody(@NonNull Function<? super ErrorCode, String> converter,
                                                  @NonNull Function<D, T> mapper) {
        return toResponseBody(converter.apply(errorCode), mapper);
    }

    public <T> HttpResponseBody<T> toResponseBody(@NonNull Function<D, T> mapper) {
        return toResponseBody((String) null, mapper);
    }

    public <T> HttpResponseBody<T> toResponseBody(String message, @NonNull Supplier<T> supplier) {
        if (errorCode.success()) {
            return HttpResponseBody.success(message, supplier.get());
        }
        return HttpResponseBody.of(errorCode(), message, supplier.get());
    }

    public <T> HttpResponseBody<T> toResponseBody(@NonNull Supplier<T> supplier) {
        return toResponseBody((String) null, supplier);
    }

    public <T> HttpResponseBody<T> toResponseBody(@NonNull ErrorCodeMessageProvider errorCodeMessageProvider,
                                                  @NonNull Supplier<T> supplier) {
        return toResponseBody(errorCodeMessageProvider.getMessage(errorCode), supplier);
    }

    public <T> HttpResponseBody<T> toResponseBody(@NonNull Function<? super ErrorCode, String> converter,
                                                  @NonNull Supplier<T> supplier) {
        return toResponseBody(converter.apply(errorCode), supplier);
    }

    public <X extends Throwable> D orElseThrow(@NonNull Supplier<? extends X> exceptionSupplier) throws X {
        if (data != null) {
            return data;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public boolean isPresent() {
        return data != null;
    }

    public boolean isEmpty() {
        return data == null;
    }

    public void ifPresent(Consumer<? super D> action) {
        if (data != null) {
            action.accept(data);
        }
    }

    public static <D> Result<D> success(D data) {
        return new Result<>(CommonErrorCode.SUCCESS, data);
    }

    public static <D> Result<D> success() {
        return (Result<D>) SUCCESS;
    }

    public static <D> Result<D> of(ErrorCode code, D data) {
        return new Result<>(code, data);
    }

    public static <D> Result<D> of(ErrorCode code) {
        return new Result<>(code, null);
    }

    private static final Result<?> SUCCESS = new Result<>(CommonErrorCode.SUCCESS, null);

}
