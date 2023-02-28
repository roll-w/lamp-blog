package space.lingu.lamp.fs;

import space.lingu.NonNull;

import java.io.IOException;

/**
 * Local file server, but not a true server.
 * <p>
 * Upload or download methods in the class will
 * only simply copies file.
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
