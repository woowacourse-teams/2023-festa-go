package com.festago.upload.util;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class UriUploadFileIdParser {

    private UriUploadFileIdParser() {
    }

    /**
     * 인자로 들어온 URI 형식의 문자열에 대해 UUID 형식의 파일 이름을 추출합니다. <br/> 해당 파일 이름은 UploadFile의 식별자로 사용됩니다. <br/> 만약 URI 형식이 아니거나, 파일
     * 이름이 UUID 형식이 아니면 빈 옵셔널을 반환합니다. <br/>
     *
     * @param uri UploadFile 식별자를 추출할 URI 문자열
     * @return 유효한 인자이면 UploadFile 식별자 값이 있는 옵셔널, 그 외 빈 옵셔널 반환
     */
    public static Optional<UUID> parse(String uri) {
        if (!StringUtils.hasText(uri)) {
            return Optional.empty();
        }
        try {
            UUID uploadFileId = getUploadFileId(uri);
            return Optional.of(uploadFileId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static UUID getUploadFileId(String uriString) {
        URI uri = URI.create(uriString);
        String path = uri.getPath();
        String fileName = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
        return UUID.fromString(fileName);
    }
}
