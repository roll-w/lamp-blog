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

package space.lingu.lamp.fs;

import space.lingu.NonNull;

import java.io.IOException;

/**
 * @author RollW
 */
public interface FileServer {
    default boolean upload(String localFilePath, String remoteFilePath) throws IOException {
        return upload(getServerFileProvider().openFile(localFilePath),
                getServerFileProvider().openFile(remoteFilePath));
    }

    default boolean upload(ServerFile localFile, ServerFile remoteFile) throws IOException {
        return upload(localFile, remoteFile, false);
    }

    boolean upload(ServerFile localFile, ServerFile remoteFile, boolean overwrite) throws IOException;

    default boolean download(String localFilePath, String remoteFilePath) throws IOException {
        return download(getServerFileProvider().openFile(localFilePath),
                getServerFileProvider().openFile(remoteFilePath));
    }

    default boolean download(ServerFile remoteFile, ServerFile localFile) throws IOException {
        return download(remoteFile, localFile, false);
    }


    boolean download(ServerFile remoteFile, ServerFile localFile, boolean overwrite) throws IOException;

    @NonNull
    ServerFileProvider getServerFileProvider();
}
