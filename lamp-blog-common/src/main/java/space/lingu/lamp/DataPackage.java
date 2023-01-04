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

/**
 * @author RollW
 */
public record DataPackage<D>(
        ErrorCode errorCode,
        D data
) {

    public static <D> DataPackage<D> success(D data) {
        return new DataPackage<>(CommonErrorCode.SUCCESS, data);
    }

    public static <D> DataPackage<D> create(ErrorCode code, D data) {
        return new DataPackage<>(code, data);
    }

    public static <D> DataPackage<D> create(ErrorCode code) {
        return new DataPackage<>(code, null);
    }
}
