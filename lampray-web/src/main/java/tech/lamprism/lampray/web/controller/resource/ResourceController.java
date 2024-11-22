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

package tech.lamprism.lampray.web.controller.resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.lamprism.lampray.web.LampraySystemApplication;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author RollW
 */
@RestController
public class ResourceController {

    @GetMapping("/static/{*path}")
    public void getResource(@PathVariable("path") String path,
                            HttpServletResponse response) throws IOException {
        ClassPathResource classPathResource =
                new ClassPathResource("/static" + path, LampraySystemApplication.class);
        String mimeType = Files.probeContentType(classPathResource.getFile().toPath());

        response.setStatus(200);
        response.setContentType(mimeType);
        ServletOutputStream outputStream = response.getOutputStream();
        classPathResource.getInputStream().transferTo(outputStream);
    }

}
