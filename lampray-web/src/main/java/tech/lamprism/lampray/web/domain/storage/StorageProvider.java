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

package tech.lamprism.lampray.web.domain.storage;


import tech.lamprism.lampray.web.domain.storage.dto.FileSystemInfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author RollW
 */
public interface StorageProvider {
    FileSystemInfile saveFile(InputStream inputStream) throws IOException;

    void getFile(String fileId, OutputStream outputStream) throws IOException;

    void getFile(String fileId, OutputStream outputStream,
                 long startBytes, long endBytes) throws IOException;

    long getFileSize(String fileId);

    FileStorage getFileStorage(String fileId);
}
