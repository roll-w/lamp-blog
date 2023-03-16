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

package space.lingu.lamp.web.controller;

import com.google.common.base.Strings;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import space.lingu.lamp.data.page.PageRequest;
import space.lingu.lamp.data.page.Pageable;
import space.lingu.lamp.web.common.ParameterFailedException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
@ControllerAdvice
public class PageRequestControllerAdvice {

    @ModelAttribute
    public PageRequest fromRequest(HttpServletRequest request) {
        String page = request.getParameter("page");
        String size = request.getParameter("size");

        try {
            int pageInt = Strings.isNullOrEmpty(page) ? 1 : Integer.parseInt(page);
            int sizeInt = Strings.isNullOrEmpty(size) ? 10 : Integer.parseInt(size);
            return new PageRequest(pageInt, sizeInt);
        } catch (NumberFormatException e) {
            throw new ParameterFailedException(e.getMessage());
        }
    }

    @ModelAttribute
    public Pageable pageableFromRequest(HttpServletRequest request) {
        return fromRequest(request);
    }
}
