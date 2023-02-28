package space.lingu.lamp.fs;

/**
 * Provides file.
 *
 * @author RollW
 */
public interface ServerFileProvider {
    ServerFile openFile(String path);

    ServerFile openFile(String parent, String path);

    ServerFile openFile(ServerFile parent, String path);
}
