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
