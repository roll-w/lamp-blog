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

package tech.lamprism.lampray.web.common;

import java.util.function.Supplier;

/**
 * @author RollW
 */
@SuppressWarnings("unused")
public class ParamValidate {
    /**
     * <p>Validate that the specified argument character sequence is
     * neither {@code null} nor a length of zero (no characters);
     * otherwise throwing an exception with the specified message.
     * <pre>{@code ParamValidate.notEmpty(myString, "The string must not be empty");}</pre>
     *
     * @param <T>     the character sequence type
     * @param chars   the character sequence to check, validated not null by this method
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws ParameterMissingException if the character sequence is null or empty
     */
    public static <T extends CharSequence> T notEmpty(final T chars,
                                                      final String message,
                                                      final Object... values) {
        if (chars == null || chars.isEmpty()) {
            throw new ParameterMissingException(message, values);
        }
        return chars;
    }

    public static <T extends CharSequence, X extends Throwable> T notEmpty(
            final T chars,
            final Supplier<X> throwableSupplier) throws X {
        if (chars == null || chars.isEmpty()) {
            throw throwableSupplier.get();
        }
        return chars;
    }

    public static <X extends Throwable> String notEmpty(
            final String string,
            final Supplier<X> throwableSupplier) throws X {
        if (string == null || string.isEmpty()) {
            throw throwableSupplier.get();
        }
        return string;
    }

    private ParamValidate() {
    }
}
