package space.lingu.lamp.fs;

import java.io.File;

/**
 * Local file provider.
 *
 * @author RollW
 */
public class LocalFileProvider implements ServerFileProvider {

    @Override
    public ServerFile openFile(String path) {
        return new LocalFile(new File(path));
    }

    @Override
    public ServerFile openFile(String parent, String path) {
        return new LocalFile(new File(parent, path));
    }

    @Override
    public ServerFile openFile(ServerFile parent, String path) {
        return new LocalFile(new File(parent.getPath(), path));
    }
}
