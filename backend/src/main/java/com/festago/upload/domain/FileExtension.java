package com.festago.upload.domain;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@RequiredArgsConstructor
public enum FileExtension {
    JPG(".jpg", MimeTypeUtils.IMAGE_JPEG),
    JPEG(".jpeg", MimeTypeUtils.IMAGE_JPEG),
    PNG(".png", MimeTypeUtils.IMAGE_PNG),
    NONE("", MimeTypeUtils.APPLICATION_OCTET_STREAM),
    ;

    private final String value;
    private final MimeType mimeType;

    public String getValue() {
        return value;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public boolean match(String value) {
        return Objects.equals(this.value, value);
    }

    public static FileExtension from(String mimeType) {
        if (mimeType == null || mimeType.isBlank()) {
            return NONE;
        }
        for (FileExtension extension : values()) {
            if (Objects.equals(extension.mimeType.toString(), mimeType)) {
                return extension;
            }
        }
        return NONE;
    }
}
