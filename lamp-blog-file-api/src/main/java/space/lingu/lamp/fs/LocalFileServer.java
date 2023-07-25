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
 * Local file server, but not a true server.
 * <p>
 * Upload or download methods in the class will
 * only simply copy file.
 *
 * @author RollW
 */
public class LocalFileServer implements FileServer {
    final LocalFileProvider localFileProvider;

    public LocalFileServer() {
        this.localFileProvider = new LocalFileProvider();
    }


    @Override
    public boolean upload(ServerFile sourceFile, ServerFile destFile, boolean overwrite) throws IOException {
        return false;
    }

    @Override
    public boolean download(ServerFile remoteFile, ServerFile localFile, boolean overwrite) throws IOException {
        return false;
    }

    @NonNull
    @Override
    public ServerFileProvider getServerFileProvider() {
        return localFileProvider;
    }
}
