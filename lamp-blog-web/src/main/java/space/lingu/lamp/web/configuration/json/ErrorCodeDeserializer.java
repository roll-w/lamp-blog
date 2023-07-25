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

package space.lingu.lamp.web.configuration.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.ErrorCodeFinderChain;

import java.io.IOException;

/**
 * @author RollW
 */
public class ErrorCodeDeserializer extends StdDeserializer<ErrorCode> {
    private final ErrorCodeFinderChain errorCodeFinderChain;

    public ErrorCodeDeserializer(ErrorCodeFinderChain codeFinderChain) {
        super(ErrorCode.class);
        this.errorCodeFinderChain = codeFinderChain;
    }

    @Override
    public ErrorCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String code = p.getValueAsString();
        return errorCodeFinderChain.findErrorCode(code);
    }
}
