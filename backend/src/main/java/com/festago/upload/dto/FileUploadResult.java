package com.festago.upload.dto;

import java.net.URI;
import java.util.UUID;

public record FileUploadResult(
    UUID uploadFileId,
    URI uploadUri
) {

}
