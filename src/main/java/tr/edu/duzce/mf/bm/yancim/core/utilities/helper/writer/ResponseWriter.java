package tr.edu.duzce.mf.bm.yancim.core.utilities.helper.writer;

import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ResponseWriter {
    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );

    private static void extractContent(byte[] content, String contentType, String contentEncoding, StringBuilder message) {
        MediaType mediaType = MediaType.valueOf(contentType);
        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            try {
                String contentString = new String(content, contentEncoding);
                Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> message.append(line).append("\n"));
            } catch (UnsupportedEncodingException e) {
                message.append(String.format("[%d bytes content]", content.length)).append("\n");
            }
        } else {
            message.append(String.format("[%d bytes content]", content.length)).append("\n");
        }
    }


    public static String getContent(HttpServletResponse response) {
        StringBuilder message = new StringBuilder();
        byte[] content = ((ContentCachingResponseWrapper) response).getContentAsByteArray();
        if (content.length > 0) {
            extractContent(content, response.getContentType(), response.getCharacterEncoding(), message);
        }
        return message.toString();
    }

}
