package space.lingu.lamp.web.controller.storage;

import com.google.common.base.Strings;
import org.springframework.http.HttpRange;
import space.lingu.lamp.web.domain.storage.FileStorage;
import space.lingu.lamp.web.domain.storage.FileType;
import space.lingu.lamp.web.domain.storage.StorageProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author RollW
 */
public final class DownloadHelper {
    public static final String ACCEPT_TYPE = "X-Lamp-Accept-Type";
    public static final String DISPOSITION_TYPE = "X-Lamp-Disposition-Type";

    private static String getEncodedFileName(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }

    private static String getResponseType(String mimeType,
                                          HttpServletRequest request) {
        String contentType = request.getHeader(ACCEPT_TYPE);
        if (Strings.isNullOrEmpty(contentType)) {
            return mimeType;
        }
        return contentType;
    }

    private static String getDispositionType(HttpServletRequest request) {
        String dispositionType = request.getHeader(DISPOSITION_TYPE);
        String param = request.getParameter("disposition");
        if (Strings.isNullOrEmpty(dispositionType)) {
            dispositionType = param;
        }
        if (Strings.isNullOrEmpty(dispositionType)) {
            return "attachment";
        }
        return dispositionType;
    }

    public static void downloadFile(FileStorage storage,
                                    String name,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    StorageProvider storageProvider) throws IOException {
        String dispositionType = getDispositionType(request);
        String contentType = getResponseType(storage.getMimeType(), request);
        response.setContentType(contentType);

        if (storage.getFileType() == FileType.TEXT) {
            response.setCharacterEncoding("utf-8");
        }
        List<HttpRange> ranges = HttpRangeUtils.tryGetsRange(request);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("Content-Security-Policy", "frame-ancestors 'self' localhost:* 127.0.0.1:*");
        response.setHeader("Content-Disposition",
                dispositionType + ";filename*=utf-8''" + getEncodedFileName(name));
        long length = storageProvider.getFileSize(storage.getFileId());

        if (!ranges.isEmpty()) {
            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(length);
            long end = range.getRangeEnd(length);
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
            response.setHeader("Content-Length", String.valueOf(end - start + 1));
            storageProvider.getFile(
                    storage.getFileId(),
                    response.getOutputStream(),
                    start,
                    end
            );
            return;
        }
        if (length > 0) {
            response.setHeader("Content-Length", String.valueOf(length));
        }
        storageProvider.getFile( storage.getFileId(), response.getOutputStream());
    }

    private DownloadHelper() {
    }
}
