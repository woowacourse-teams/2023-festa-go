package com.festago.upload.presentation;

import com.festago.upload.domain.FileOwnerType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FileOwnerTypeConverter implements Converter<String, FileOwnerType> {

    @Override
    public FileOwnerType convert(String fileOwnerType) {
        return FileOwnerType.valueOf(fileOwnerType.toUpperCase());
    }
}
